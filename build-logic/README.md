# build-logic — Convention Plugins

This included build centralises all Android Gradle configuration for **PagoDividido**.
Instead of repeating `android {}` blocks, plugin aliases, and dependency declarations in
every module, each module simply applies one or more lightweight convention plugins.

---

## Structure

```
build-logic/
├── build.gradle.kts                  # kotlin-dsl + plugin classpath deps
├── settings.gradle.kts               # repositories for build-logic itself
└── src/main/kotlin/
    ├── AndroidConfig.kt                        # SDK version constants (single source of truth)
    ├── anwera97.android.library.gradle.kts     # Base Android library convention
    ├── anwera97.android.application.gradle.kts # Base Android application convention
    ├── anwera97.android.hilt.gradle.kts        # Hilt + KSP plugin & dependencies
    └── anwera97.android.compose.gradle.kts     # Compose compiler plugin + buildFeatures
```

---

## Convention plugins

### `anwera97.android.library`
Applies `com.android.library` and sets shared defaults for every library module.

| Centralised config | Value |
|---|---|
| `compileSdk` | `AndroidConfig.COMPILE_SDK` |
| `minSdk` | `AndroidConfig.MIN_SDK` |
| `testInstrumentationRunner` | `androidx.test.runner.AndroidJUnitRunner` |
| `consumerProguardFiles` | `consumer-rules.pro` |
| Release `buildType` | ProGuard defaults, minify off |
| `compileOptions` | Java 17 |

Each library module retains only its `namespace` (and `targetSdk` where intentionally overridden).

---

### `anwera97.android.application`
Applies `com.android.application` and sets shared defaults for the app module.

| Centralised config | Value |
|---|---|
| `compileSdk` | `AndroidConfig.COMPILE_SDK` |
| `minSdk` | `AndroidConfig.MIN_SDK` |
| `targetSdk` | `AndroidConfig.TARGET_SDK` |
| `testInstrumentationRunner` | `androidx.test.runner.AndroidJUnitRunner` |
| Release `buildType` | ProGuard defaults, minify off |
| `compileOptions` | Java 17 |

The app module retains only `applicationId`, `versionCode`, `versionName`, `namespace`, and feature-specific config.

---

### `anwera97.android.hilt`
Applies `com.google.dagger.hilt.android` **and** `com.google.devtools.ksp`, then
injects the Hilt runtime and compiler dependencies via the version catalog.

```groovy
// Before — repeated in every module
alias(libs.plugins.hilt)
alias(libs.plugins.ksp)
// ...
implementation libs.hilt.android
ksp libs.hilt.compiler

// After — one line
id 'anwera97.android.hilt'
```

> **Note:** `domain` and `infra` were previously missing the Hilt Gradle plugin.
> This convention retroactively corrected that gap.

---

### `anwera97.android.compose`
Applies `org.jetbrains.kotlin.plugin.compose` and enables `buildFeatures.compose = true`.
Compatible with both `ApplicationExtension` and `LibraryExtension` (AGP 9+).

```groovy
// Before
alias(libs.plugins.kotlin.compose)
android { buildFeatures { compose true } }

// After
id 'anwera97.android.compose'
```

---

### `AndroidConfig.kt`
Single source of truth for SDK version integers. Both library and application convention
plugins read from here — bumping an SDK level is a **one-line change**.

```kotlin
object AndroidConfig {
    const val COMPILE_SDK = 35
    const val MIN_SDK     = 21
    const val TARGET_SDK  = 35
}
```

---

## Module plugin composition

| Module | Convention plugins applied |
|---|---|
| `:domain` | `anwera97.android.library` · `anwera97.android.hilt` |
| `:data` | `anwera97.android.library` · `anwera97.android.hilt` |
| `:infra` | `anwera97.android.library` · `anwera97.android.hilt` |
| `:presentation` | `anwera97.android.application` · `anwera97.android.hilt` · `anwera97.android.compose` |

---

## Build-time comparison

Measured on the same machine with a warm Gradle daemon.
`--rerun-tasks` forces all tasks to re-execute regardless of up-to-date checks (cold build simulation).

### Cold build (`assembleDebug --rerun-tasks`)

| Branch | Time | Tasks |
|---|---|---|
| `develop` | **12 s** | 135 |
| `feature/plugin-conventions` | **15 s** | 146 |
| Δ | +3 s | +11 tasks |

The 11 extra tasks are build-logic compilation tasks (Kotlin DSL plugin generation,
precompiled script compilation, accessor generation). These only execute when a
convention plugin file is modified — **not on every developer build**.

### Warm/incremental build (`assembleDebug`, nothing changed)

| Branch | Time | Tasks |
|---|---|---|
| `develop` | **1 s** | 135 up-to-date |
| `feature/plugin-conventions` | **~844 ms** | 146 up-to-date |
| Δ | −156 ms | — |

Incremental build speed is identical in practice. The slight improvement on the
convention branch comes from simpler module `build.gradle` files having less
configuration logic to evaluate.

### Interpretation

- The **+3 s cold overhead** is the cost of compiling `build-logic` itself. It is
  amortised across all subsequent builds until a convention plugin is changed.
- As the project gains more modules, the overhead stays **constant** (build-logic
  compiles once) while the savings per module compound (each new module avoids
  ~30 lines of duplicated Android config).

---

## Future improvements

The following items are natural next steps to expand and harden this setup:

### Short-term

- [ ] **Kotlin version constant in `AndroidConfig`** — Add `KOTLIN_JVM_TARGET` alongside
      the SDK constants so `compileOptions` source/target compatibility is also a
      single source of truth.
- [ ] **`anwera97.android.testing` convention** — Centralise the repeated test dependency
      blocks (`junit`, `mockito-core`, `mockito-kotlin`, `coroutines-test`,
      `arch-core-testing`) that appear across `:domain`, `:data`, and `:presentation`.
- [ ] **`anwera97.android.library.hilt` composite** — A combined plugin that applies
      both `anwera97.android.library` + `anwera97.android.hilt` for the common case
      where every library module uses Hilt. Reduces each library to a single plugin line.

### Medium-term

- [ ] **Migrate `build.gradle` → `build.gradle.kts`** — Convert all module build files
      from Groovy DSL to Kotlin DSL for full IDE support (auto-complete, refactoring,
      type safety) and consistency with build-logic.
- [ ] **Version catalog access in build-logic** — Wire `../gradle/libs.versions.toml`
      into `build-logic/settings.gradle.kts` via `versionCatalogs { from(files(…)) }`
      so build-logic classpath dependencies (`agp`, `ksp`, `hilt` versions) can also
      be read from the catalog instead of being hardcoded strings.
- [ ] **Enable Gradle configuration cache** — The project is already close to compatible.
      Add `org.gradle.configuration-cache=true` to `gradle.properties` and fix any
      remaining violations to unlock significant configuration-phase speedups.

### Long-term

- [ ] **Build scan / CI build metrics** — Baseline the build times in CI with
      Develocity (or the free Gradle Build Scan) so regressions are caught automatically.
- [ ] **`anwera97.android.room` convention** — If more modules adopt Room in the future,
      extract the Room dependency bundle + KSP compiler declaration into its own plugin.
- [ ] **Strict dependency visibility** (`moduleDependencies` / `DependencyGuard`) —
      Enforce which modules can depend on which, preventing accidental coupling between
      `:presentation` and `:data` bypassing `:domain`.

