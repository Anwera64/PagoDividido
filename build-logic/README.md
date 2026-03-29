# build-logic — Convention Plugins

> AI context docs: `build-logic/GRADLE_AI_CONTEXT.md` (Gradle/conventions) and `build-logic/UNIT_TEST_AI_CONTEXT.md` (unit tests).

This included build centralises all Android Gradle configuration for **PagoDividido**.
Instead of repeating `android {}` blocks, plugin aliases, and dependency declarations in
every module, each module simply applies one or more lightweight convention plugins.

---

## Structure

```
build-logic/
├── GRADLE_AI_CONTEXT.md             # AI guide for Gradle and convention-plugin changes
├── UNIT_TEST_AI_CONTEXT.md          # AI guide for unit-test changes and test quality rules
├── build.gradle.kts                  # kotlin-dsl + plugin classpath deps
├── settings.gradle.kts               # repositories for build-logic itself
└── src/main/kotlin/
    ├── AndroidConfig.kt                        # SDK version constants (single source of truth)
    ├── pagodividido.android.library.gradle.kts     # Base Android library convention
    ├── pagodividido.android.application.gradle.kts # Base Android application convention
    ├── pagodividido.android.hilt.gradle.kts        # Hilt + KSP plugin & dependencies
    └── pagodividido.android.compose.gradle.kts     # Compose compiler plugin + buildFeatures
```

---

## Convention plugins

### `pagodividido.android.library`
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

### `pagodividido.android.application`
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

### `pagodividido.android.hilt`
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
id 'pagodividido.android.hilt'
```

> **Note:** Hilt is applied only where needed (`:data`, `:presentation`).
> `:domain` keeps constructor injection via `javax.inject` without Hilt plugin overhead.

---

### `pagodividido.android.compose`
Applies `org.jetbrains.kotlin.plugin.compose` and enables `buildFeatures.compose = true`.
Compatible with both `ApplicationExtension` and `LibraryExtension` (AGP 9+).

```groovy
// Before
alias(libs.plugins.kotlin.compose)
android { buildFeatures { compose true } }

// After
id 'pagodividido.android.compose'
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
| `:domain` | `pagodividido.android.library` |
| `:data` | `pagodividido.android.library` · `pagodividido.android.hilt` |
| `:presentation` | `pagodividido.android.application` · `pagodividido.android.hilt` · `pagodividido.android.compose` |

> Note: `:infra` has been removed; active modules are `:presentation`, `:data`, and `:domain`.

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

## Hilt dependency injection best practices

This project follows official Hilt patterns and modern Android DI conventions. The following
ensures clean dependency resolution and avoids common Hilt pitfalls:

### 1. **Module structure and dependency flow**

```
domain/              (pure Kotlin, no Android)
  ├── models/
  ├── repositories/  (interfaces only)
  └── usecases/      (@Inject-ready, depend on repositories)

data/                (Android library, provides implementations)
  ├── entities/
  ├── dao/
  ├── repository/    (@Inject constructors implementing domain interfaces)
  └── RepositoryModule.kt  (@Binds - maps interfaces → implementations)

presentation/        (Android app, uses everything)
  └── ui/activities, viewmodels, etc.
```

**Key principle:** presentation depends on `data`, not vice versa. This allows
presentation's Hilt component to discover and wire `RepositoryModule` bindings.

### 2. **Repository binding pattern**

Use abstract `@Binds` methods (not `@Provides`) for interface→implementation mapping.
This is more efficient (no instance creation in the module itself):

```kotlin
// data/src/main/java/com/anwera97/data/RepositoryModule.kt
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindTripRepository(impl: TripRepositoryImpl): TripRepository

    @Binds
    abstract fun bindCompanionRepository(impl: CompanionRepositoryImpl): CompanionRepository

    @Binds
    abstract fun bindExpenditureRepository(impl: ExpenditureRepositoryImpl): ExpenditureRepository
}
```

Each implementation must have an `@Inject` constructor:

```kotlin
class TripRepositoryImpl @Inject constructor(
    private val tripDao: TripDao
) : TripRepository { … }
```

### 3. **Hilt components and scopes**

- **SingletonComponent** (app lifetime) — Use for repositories, DAOs, database instances.
- **ActivityRetainedComponent** (across configuration changes) — For business logic.
- **ActivityComponent** (per activity) — For activity-scoped services.
- **ViewModelComponent** — Automatically used by `@HiltViewModel` for lifecycle-aware injection.

**Current project setup:**
- Database + DAOs: `@Singleton` in `DataModule` → `SingletonComponent`
- Repositories: `@Singleton` via `@Binds` in `RepositoryModule` → `SingletonComponent`
- UseCases: `@Inject` constructor, scoped by their consumer (usually `@HiltViewModel`)
- ViewModels: `@HiltViewModel` → `ViewModelComponent` (provides lifecycle awareness)

### 4. **ViewModel injection**

All ViewModels in `:presentation` are annotated with `@HiltViewModel` and use Hilt's
built-in `ViewModelComponent` scope:

```kotlin
@HiltViewModel
class MainViewModel @Inject constructor(
    private val tripsUseCase: TripsUseCase  // injected by Hilt
) : ViewModel() {
    val trips: LiveData<List<TripModel>> by lazy {
        tripsUseCase.getAllTrips().asLiveData(viewModelScope.coroutineContext)
    }
}
```

Activities retrieve these via `val viewModel: MainViewModel by viewModels()` — Hilt
automatically creates and retains them.

### 5. **Android entry point**

The application class must be annotated with `@HiltAndroidApp`:

```kotlin
// presentation/src/main/java/com/anwera97/pagodividido/PagoDivididoApp.kt
@HiltAndroidApp
class PagoDivididoApp : Application()
```

This triggers Hilt code generation and initialises the dependency graph at app startup.

### 6. **Common issues and fixes**

#### Issue: "Cannot be provided without an @Provides-annotated method"

This means Hilt cannot find a way to construct the requested type. Common causes:

1. **Missing dependency** — Module is not in the dependency path. Example:
   - `:presentation` depended only on `:domain`, not `:data`.
   - `:data/RepositoryModule` was never discovered.
   - **Fix:** Ensure presentation depends on data: `implementation project(':data')`

2. **Missing @Inject constructor** — Repository implementation lacks it.
   - **Fix:** Add `@Inject constructor(…)` to implementation class.

3. **Missing @Binds method** — Interface has no mapping to implementation.
   - **Fix:** Add abstract `@Binds` method in a `@Module` in the provider module.

4. **Wrong scope** — Binding installed in wrong component.
   - **Fix:** Ensure `@InstallIn(SingletonComponent::class)` for app-level bindings.

#### Issue: "Multiple binding solutions"

Hilt found more than one way to provide a type.

- **Fix:** Remove duplicate `@Provides` or `@Binds` methods, or disambiguate with
  `@Qualifier` annotations.

### 7. **Testing considerations**

For unit/integration tests, use `@HiltAndroidTest` and inject test fakes:

```kotlin
@HiltAndroidTest
class RepositoryTest {
    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var tripRepository: TripRepository

    @Before
    fun setup() { hiltRule.inject() }

    @Test
    fun testInsertTrip() { … }
}
```

For module replacement in tests, use `@UninstallModules` and custom test modules
via `@TestInstallIn`.

### 8. **Future enhancements**

Consider these when expanding the project:

- **@Qualifier annotations** — If multiple implementations of the same interface are needed.
  ```kotlin
  @Qualifier annotation class Local
  @Qualifier annotation class Remote
  
  @Binds @Local
  abstract fun bindLocalTripRepository(…): TripRepository
  ```
- **Factory pattern** — For complex object creation beyond constructor injection.
  ```kotlin
  @Module
  @InstallIn(SingletonComponent::class)
  object ComplexModule {
      @Provides
      @Singleton
      fun provideComplexService(…): ComplexService = ComplexService(…)
  }
  ```
- **@EntryPoint** — For injecting into objects not constructed by Hilt (e.g., plugins).

---

## Future improvements

The following items are natural next steps to expand and harden this setup:

### Short-term

- [x] **Fixed: Presentation depends on data** — `:presentation` now correctly depends on
      `:data`, allowing Hilt to discover `RepositoryModule` bindings at compile time.
      Previously, Hilt could not resolve repository implementations, causing DI failures.
- [ ] **Kotlin version constant in `AndroidConfig`** — Add `KOTLIN_JVM_TARGET` alongside
      the SDK constants so `compileOptions` source/target compatibility is also a
      single source of truth.
- [ ] **`pagodividido.android.testing` convention** — Centralise the repeated test dependency
      blocks (`junit`, `mockito-core`, `mockito-kotlin`, `coroutines-test`,
      `arch-core-testing`) that appear across `:domain`, `:data`, and `:presentation`.
- [ ] **`pagodividido.android.library.hilt` composite** — A combined plugin that applies
      both `pagodividido.android.library` + `pagodividido.android.hilt` for the common case
      where every library module uses Hilt. Reduces each library to a single plugin line.

### Medium-term

- [ ] **Migrate `build.gradle` → `build.gradle.kts`** — Convert all module build files
      from Groovy DSL to Kotlin DSL for full IDE support (auto-complete, refactoring,
      type safety) and consistency with build-logic.
- [x] **Version catalog access in build-logic** — `build-logic` now imports
      `../gradle/libs.versions.toml` and reads `agp`, `ksp`, `hilt`, and
      `kotlin-compose-plugin` versions from the shared catalog.
- [ ] **Enable Gradle configuration cache** — The project is already close to compatible.
      Add `org.gradle.configuration-cache=true` to `gradle.properties` and fix any
      remaining violations to unlock significant configuration-phase speedups.

### Long-term

- [ ] **Build scan / CI build metrics** — Baseline the build times in CI with
      Develocity (or the free Gradle Build Scan) so regressions are caught automatically.
- [ ] **`pagodividido.android.room` convention** — If more modules adopt Room in the future,
      extract the Room dependency bundle + KSP compiler declaration into its own plugin.
- [ ] **Strict dependency visibility** (`moduleDependencies` / `DependencyGuard`) —
      Enforce which modules can depend on which, preventing accidental coupling between
      `:presentation` and `:data` bypassing `:domain`.

