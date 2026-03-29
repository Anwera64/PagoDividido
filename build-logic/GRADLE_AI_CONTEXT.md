# AI Gradle Context (PagoDividido)

Last updated: 2026-03-29

This file is the source of truth for AI-assisted Gradle changes in this repository.

## Mandatory workflow for any Gradle change

Before editing any `*.gradle`, `*.gradle.kts`, `settings.gradle`, `settings.gradle.kts`,
or `gradle/libs.versions.toml`:

1. Read this file fully.
2. Identify whether the change belongs in a module build file or in `build-logic` convention plugins.
3. Prefer convention plugin updates over per-module duplication.
4. After the change, run a focused Gradle verification command.
5. Update this file (and `build-logic/README.md` if needed) with the new rule or decision.

If there is a conflict between module files and this file, align the modules to this file unless
there is an explicit architectural decision to change direction.

## Current Gradle architecture

- Included modules in `settings.gradle`: `:app`, `:presentation`, `:data`, `:domain`.
- `:infra` has been removed from the repository.
- Convention plugins are implemented in `build-logic/src/main/kotlin`.
- Shared versions come from `gradle/libs.versions.toml`.
- `build-logic` imports the root version catalog via `build-logic/settings.gradle.kts`.

## Convention plugins in use

- `pagodividido.android.application`
  - Applies `com.android.application`.
  - Centralizes app defaults: `compileSdk`, `minSdk`, `targetSdk`, Java 17, release build type.
- `pagodividido.android.library`
  - Applies `com.android.library`.
  - Centralizes library defaults: `compileSdk`, `minSdk`, Java 17, release build type.
- `pagodividido.android.hilt`
  - Applies Hilt + KSP and adds Hilt dependencies.
  - Use only where Hilt code generation is required.
- `pagodividido.android.compose`
  - Applies Kotlin Compose plugin and enables Compose features.

## Module policy

- `:presentation`
  - Plugins: `library`, `hilt`, `compose` conventions.
  - Depends on `:domain` and exposes UI layer code.
- `:app`
  - Plugins: `application`, `hilt` conventions.
  - Owns launcher manifest and `@HiltAndroidApp`.
  - Must depend on `:data` and `:presentation` to compose the runtime DI graph.
- `:data`
  - Plugins: `library`, `hilt` conventions.
  - Owns Room database/DAO/providers and repository bindings.
- `:domain`
  - Plugin: `library` convention only.
  - No Hilt plugin by default.
  - Uses `javax.inject` annotations and explicit coroutine dependency.

## Important DI decision

`TripRepository`, `CompanionRepository`, and `ExpenditureRepository` bindings are declared in
`data/src/main/java/com/anwera97/data/RepositoryModule.kt`.

Because of this, `:app` must keep `implementation project(':data')` to allow Hilt to
resolve repository bindings used by ViewModels in `:presentation`.

## Build-logic policy

- Do not hardcode AGP/KSP/Hilt/Compose plugin versions inside `build-logic/build.gradle.kts`.
- Read those versions from the shared version catalog.
- Keep `AndroidConfig.kt` as the single source of truth for SDK values.

## Change log (plugin conventions)

- Standardized convention plugin namespace to `pagodividido`.
- Migrated modules to convention plugins (application/library/hilt/compose).
- Extracted SDK values into `AndroidConfig.kt`.
- Extracted Hilt + KSP setup into `pagodividido.android.hilt` convention.
- Fixed Hilt missing bindings by enforcing `:app -> :data` dependency.
- Reduced domain overhead by removing unnecessary Hilt plugin from `:domain`.
- Added explicit `javax.inject` and `kotlinx.coroutines.core` in `:domain`.
- Centralized build-logic plugin classpath versions using the root version catalog.
- Replaced deprecated `buildDir` usage with `layout.buildDirectory` in root `build.gradle`.

## Quick verification commands

Use the smallest command that validates your change:

- App + DI sanity:
  - `./gradlew :app:assembleDebug --no-daemon`
- Domain compile/tests:
  - `./gradlew :domain:test --no-daemon`
- Full project check (heavier):
  - `./gradlew build --no-daemon`

## Updating this file

Update this file immediately when:

- A convention plugin behavior changes.
- A module adds/removes a convention plugin.
- A module dependency changes in a way that affects DI or build graph.
- Version source changes (catalog vs hardcoded).
- A Gradle deprecation migration is applied.

