# Android CI — Workflow Explanation

> Last updated: 2026-03-31 — removed `gradle-home-cache-cleanup` (incompatible with project's Gradle version); added XML check guard before `dorny/test-reporter` to handle modules with no unit tests; recorded first real pipeline run results

This document explains every job and decision in `android.yml` so future developers can understand, maintain, and extend the pipeline safely.

---

## Triggers

```yaml
on:
  push:
    branches: [ master, develop ]
  pull_request:
    branches: [ master, develop ]
```

The pipeline fires on every **push** to `master`/`develop`, and on every **pull request** targeting those same branches.

---

## Permissions (global)

```yaml
permissions:
  checks: write
  pull-requests: write
  actions: write
```

| Permission | Why it's needed |
|---|---|
| `checks: write` | `dorny/test-reporter` needs this to create Check annotations on each commit |
| `pull-requests: write` | `dorny/test-reporter` needs this to comment on PRs |
| `actions: write` | The `cleanup` job needs this to call the GitHub cache DELETE API |

These are declared at the **workflow level** so every job inherits them.

---

## Job 1 — `compile` (warm build cache)

```yaml
compile:
  runs-on: ubuntu-latest
  steps:
    - uses: gradle/actions/setup-gradle@v3
      with:
        cache-read-only: false
    - run: ./gradlew assembleDebug --build-cache
```

**This is the most important job.** It runs before everything else and its sole purpose is to compile the whole project once and save the results to the GitHub Actions cache.

- `cache-read-only: false` → this is the **only** job allowed to **write** to the cache. All others are read-only.
- `--build-cache` → tells Gradle to store every task's output in the build cache (keyed by its inputs hash). Downstream jobs that restore this cache will see all compilation tasks as `UP-TO-DATE` and skip them.
- `assembleDebug` → compiles all 4 modules (`:domain` → `:data` → `:presentation` → `:app`) in dependency order.

> **Why `gradle-home-cache-cleanup` is not used:** this option injects an init script that reads the `removeUnusedEntriesOlderThan` property, which is **write-only** in the Gradle version this project uses — causing an immediate build failure. It is also unnecessary here because the `cleanup` job already deletes all caches after every run, leaving nothing to prune.

**Without this job**: each of the 4 test runners + lint + build job would each recompile the whole tree = 6× redundant work.  
**With this job**: compiled once, reused 6× from cache.

---

## Job 2 — `lint`

```yaml
lint:
  needs: compile
  steps:
    - uses: gradle/actions/setup-gradle@v3
      with:
        cache-read-only: true
    - run: ./gradlew lintDebug --build-cache
    - uses: actions/upload-artifact@v4
      with:
        name: lint-reports
        path: '**/build/reports/lint-results-*.html'
        if-no-files-found: warn
```

- `needs: compile` → waits for the warm cache before starting.
- `cache-read-only: true` → restores the cache written by `compile` but **does not write** back. This prevents cache key collisions between parallel jobs.
- `lintDebug` → runs Android Lint on the `debug` variant for all modules.
- The HTML reports from every module (`**/build/reports/lint-results-*.html`) are uploaded as a downloadable artifact named `lint-reports`. You can grab them from the GitHub Actions run page.

---

## Job 3 — `unit-tests` (matrix, parallel)

```yaml
unit-tests:
  needs: compile
  strategy:
    fail-fast: false
    matrix:
      module: [ domain, data, presentation, app ]
  steps:
    - run: ./gradlew :${{ matrix.module }}:test --build-cache

    - name: Check for test result files · :${{ matrix.module }}
      id: check_results
      if: always()
      run: |
        if find "${{ matrix.module }}/build/test-results" -name "*.xml" -type f 2>/dev/null | grep -q .; then
          echo "found=true" >> $GITHUB_OUTPUT
        else
          echo "found=false" >> $GITHUB_OUTPUT
        fi

    - uses: dorny/test-reporter@v1
      if: always() && steps.check_results.outputs.found == 'true'
      with:
        name: "Unit Tests · :${{ matrix.module }}"
        path: "${{ matrix.module }}/build/test-results/**/*.xml"
        reporter: java-junit
        fail-on-error: true

    - uses: actions/upload-artifact@v4
      with:
        name: test-reports-${{ matrix.module }}
        path: "${{ matrix.module }}/build/reports/tests/"
```

- **Matrix strategy** → GitHub spins up **4 separate runners simultaneously**, one per module. They all start as soon as `compile` finishes.
- `fail-fast: false` → if `:data` tests fail, `:presentation`, `:domain`, and `:app` **keep running**. You see all failures at once instead of stopping at the first one.
- `cache-read-only: true` → same as lint — restores compiled classes but does not write.
- `./gradlew :module:test` → only runs tests for that specific module. Because production classes are already in the build cache, Gradle only needs to compile the test sources for that module — much faster.
- **Check step** → probes `build/test-results` for XML files before running the reporter. `dorny/test-reporter@v1` has no built-in tolerance for an empty result set — it crashes with *"No test report files were found"* if a module has no tests (e.g. `:app`). The `found` output gates the reporter so it is silently skipped for empty modules and runs normally for modules that have tests.
- `dorny/test-reporter@v1` → reads the JUnit XML files and publishes them as a **GitHub Check** on the commit/PR. Each module gets its own named Check entry in the Checks tab with pass/fail per individual test method.
- `upload-artifact` → saves the richer HTML report per module as a downloadable artifact (`test-reports-domain`, `test-reports-data`, etc.) with `if: always()`.

---

## Job 4 — `build`

```yaml
build:
  needs: [ lint, unit-tests ]
  steps:
    - run: ./gradlew assembleDebug --build-cache
    - uses: actions/upload-artifact@v4
      with:
        name: debug-apk
        path: app/build/outputs/apk/debug/*.apk
```

- `needs: [lint, unit-tests]` → this is the **gate**. The APK is only produced when both lint is clean and all 4 module test suites pass.
- Because `compile` already ran `assembleDebug` and the cache is restored, Gradle sees all tasks as `UP-TO-DATE` → this step completes in **seconds**.
- The resulting `.apk` is uploaded as a `debug-apk` artifact available on the run page.

---

## Job 5 — `cleanup`

```yaml
cleanup:
  needs: build
  if: always()
  steps:
    - name: Delete Gradle caches for this branch
      env:
        GH_TOKEN: ${{ secrets.GITHUB_TOKEN }}
      run: |
        caches=$(gh api "/repos/.../actions/caches?ref=${{ github.ref }}" \
          --jq '.actions_caches[] | select(.key | startswith("gradle")) | .id')
        for id in $caches; do
          gh api --method DELETE "/repos/.../actions/caches/$id"
        done
```

- `needs: build` → waits for the very last job to finish.
- `if: always()` → **critical**. Runs regardless of whether upstream jobs passed, failed, or were skipped. This guarantees cleanup always happens.
- Uses the `gh` CLI (pre-installed on all GitHub-hosted runners) to call the REST API and delete **every `gradle-*` cache entry** scoped to the current branch ref.
- Why per-branch? GitHub Actions cache is **branch-scoped**. A cache written on `feature/x` is invisible to `develop`. Deleting by `ref` only cleans up that branch's caches, never touching other branches.
- Result: the next pipeline run on this branch starts from a **known-clean state** — no risk of stale compiled outputs polluting future runs.

---

## Full pipeline flow

```
push / PR
    │
    ▼
 compile  ──────────────────────────────────────────────────────┐
 (writes cache)                                                 │
    │                                                           │
    ├──────────┬──────────────┬──────────────┬──────────────┐  │
    ▼          ▼              ▼              ▼              ▼  │
  lint    tests·domain   tests·data   tests·presentation  tests·app
             (all read-only cache, run in parallel)
    │          │              │              │              │
    └──────────┴──────────────┴──────────────┴──────────────┘
                                   │
                                   ▼
                                 build
                          (read cache → ~instant)
                                   │
                                   ▼
                                cleanup
                           (delete branch caches)
```

---

## Key tradeoff to know

Deleting the cache after every run means each pipeline **always does a full cold compile** in the `compile` job. This is intentional — it is your guarantee of a clean state. If you ever want to trade that guarantee for faster `compile` times (by reusing the previous run's cache for incremental builds), remove the `cleanup` job.

---

## How to add a new module

1. Add the module to `settings.gradle`.
2. Apply the appropriate convention plugin (`pagodividido.android.library`, etc.) in its `build.gradle`.
3. Add the module name to the matrix in `android.yml`:

```yaml
matrix:
  module: [ domain, data, presentation, app, your-new-module ]
```

That's it — the new module automatically gets its own parallel test runner, its own Check entry on PRs, and its own downloadable HTML report.

---

## Real pipeline results (2026-03-31)

First successful run recorded after the warm build cache strategy was fully applied.  
All 11 checks passed on a pull request.

| Job | Result | Duration |
|---|---|---|
| Compile (warm build cache) | ✅ Successful | 4 min |
| Lint | ✅ Successful | 3 min |
| Tests · :domain | ✅ Successful | 2 min |
| Tests · :data | ✅ Successful | 2 min |
| Tests · :presentation | ✅ Successful | 3 min |
| Tests · :app | ✅ Successful | 3 min |
| Build Debug APK | ✅ Successful | 4 min |
| Cleanup Gradle Caches | ✅ Successful | 2 sec |

### Observations

- **`compile` took 4 min** — this is the only job doing a full cold build. Every other job restored compiled outputs from its cache and skipped recompilation.
- **`lint` and all 4 test runners ran in parallel** (2–3 min each) while sharing the same warm cache. Without the cache each would have spent ~4 min compiling before even starting its actual work.
- **`build` also took 4 min** — `assembleDebug` was `UP-TO-DATE` from the cache; the time was dominated by the GitHub Actions runner setup and Gradle daemon startup, not actual compilation.
- **`cleanup` took 2 sec** — deleting the branch caches via the GitHub API is near-instant.
- **Total wall-clock time ≈ 4 min + 3 min + 4 min = ~11 min** (compile → parallel jobs → build → cleanup in sequence), compared to an estimated ~24 min (6 × 4 min) if every job compiled independently with no cache.

