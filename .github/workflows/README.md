# Android CI — Workflow Explanation

> Last updated: 2026-03-31 — removed warm build cache (`compile` job) after benchmarking proved it increases wall-clock time; current pipeline starts all jobs in parallel immediately

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

## Job 1 — `lint`

```yaml
lint:
  runs-on: ubuntu-latest
  steps:
    - uses: gradle/actions/setup-gradle@v3
      with:
        cache-read-only: false
    - run: ./gradlew lintDebug --build-cache
    - uses: actions/upload-artifact@v4
      with:
        name: lint-reports
        path: '**/build/reports/lint-results-*.html'
        if-no-files-found: warn
```

- Starts immediately on push/PR — no dependency on any other job.
- `cache-read-only: false` → each job manages its own Gradle home cache (downloaded dependencies). There is no shared pre-warmed cache.
- `lintDebug` → runs Android Lint on the `debug` variant for all modules.
- HTML reports from every module are uploaded as a downloadable artifact named `lint-reports`.

---

## Job 2 — `unit-tests` (matrix, parallel)

```yaml
unit-tests:
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

- Also starts immediately — **no dependency on any other job**, runs fully in parallel with `lint`.
- **Matrix strategy** → GitHub spins up **4 separate runners simultaneously**, one per module.
- `fail-fast: false` → if `:data` tests fail, `:presentation`, `:domain`, and `:app` **keep running**. You see all failures at once instead of stopping at the first one.
- `./gradlew :module:test` → only runs tests for that specific module.
- **Check step** → probes `build/test-results` for XML files before running the reporter. `dorny/test-reporter@v1` has no built-in tolerance for an empty result set — it crashes with *"No test report files were found"* if a module has no tests (e.g. `:app`). The `found` output gates the reporter so it is silently skipped for empty modules and runs normally for modules that have tests.
- `dorny/test-reporter@v1` → publishes results as a **GitHub Check** on the commit/PR with pass/fail per individual test method.
- `upload-artifact` → saves the HTML report per module as a downloadable artifact with `if: always()`.

---

## Job 3 — `build`

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
- The resulting `.apk` is uploaded as a `debug-apk` artifact available on the run page.

---

## Job 4 — `cleanup`

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
    ├──────────┬──────────────┬──────────────┬──────────────┐
    ▼          ▼              ▼              ▼              ▼
  lint    tests·domain   tests·data   tests·presentation  tests·app
          (all start immediately in parallel, no waiting)
    │          │              │              │              │
    └──────────┴──────────────┴──────────────┴──────────────┘
                                   │
                                   ▼
                                 build
                                   │
                                   ▼
                                cleanup
                           (delete branch caches)
```

---

## Key tradeoff to know

> **Why there is no `compile` (warm build cache) job**
>
> A `compile` job was benchmarked (see results below) and **removed** because it increased total
> wall-clock time. The compile job forced all parallel jobs to wait ~4 min before starting.
> Since each module compiles fast on its own runner, starting everything in parallel immediately
> is faster end-to-end, even though each runner does its own compilation.
>
> If modules grow significantly in size and compilation time per runner exceeds ~5 min,
> re-introducing the `compile` job should be reconsidered.

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

## Real pipeline results & benchmark

### Run 1 — With warm build cache

A `compile` job ran first, wrote the Gradle build cache, then all downstream jobs restored from it.

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

**Total wall-clock time ≈ 11 min** (compile 4m → parallel jobs 3m → build 4m → cleanup 2s)

---

### Run 2 — Without warm build cache ✅ current approach

`compile` job removed. All jobs start immediately in parallel from the first second of the run.

| Job | Result | Duration |
|---|---|---|
| Lint | ✅ Successful | 3 min |
| Tests · :domain | ✅ Successful | 2 min |
| Tests · :data | ✅ Successful | 2 min |
| Tests · :presentation | ✅ Successful | 3 min |
| Tests · :app | ✅ Successful | 3 min |
| Build Debug APK | ✅ Successful | 4 min |
| Cleanup Gradle Caches | ✅ Successful | 4 sec |

**Total wall-clock time ≈ 7 min** (parallel jobs 3m → build 4m → cleanup 4s)

---

### Conclusion

| | With warm cache | Without warm cache |
|---|---|---|
| Wall-clock time | ~11 min | ~7 min ✅ |
| Individual job times | 2–3 min (cache restored) | 2–3 min (same) |
| Total runner-minutes | ~1 runner × 4m + 5 × 3m + 4m = ~23 min | 5 × 3m + 4m = ~19 min ✅ |
| Complexity | Higher (cache read/write coordination) | Lower ✅ |

**The warm cache added 4 min of forced sequential waiting without reducing individual job times.**
Each module is small enough that its own compilation fits comfortably inside the job's runtime.
The no-warm-cache approach wins on both wall-clock time and total runner-minute consumption.
