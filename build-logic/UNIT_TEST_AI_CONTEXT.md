# Unit Test AI Context (PagoDividido)

Last updated: 2026-03-29

This file is the source of truth for AI-assisted unit-test changes in this repository.

## Scope

Use this guide for files under:

- `domain/src/test/**`
- `data/src/test/**`
- `presentation/src/test/**`

This guide does not cover instrumentation tests (`androidTest`).

## Mandatory workflow for any unit-test change

1. Run the smallest relevant test first (class or module level).
2. Identify whether failure is test flakiness, assertion mismatch, or production bug.
3. Prefer minimal test fixes that preserve behavior intent.
4. Change production code only when behavior is truly incorrect.
5. Re-run impacted tests, then run full unit-test baseline before finishing.

## Unit-test quality standards

- Tests must be deterministic (no wall-clock coupling, random values, or order dependence).
- Use clear Arrange-Act-Assert structure.
- Test names should describe behavior (`when ... then ...` style or equivalent).
- Assert business-relevant fields, not incidental implementation details.
- Keep one primary reason to fail per test.

## Mockito and coroutine guidelines

- When using Mockito argument captors, avoid mixing raw values and matchers.
  - If one argument uses a matcher/captor, use matchers for all arguments.
- Avoid direct equality checks on objects with runtime-generated fields (for example `Date()`).
  - Capture argument and assert stable fields.
- Use `runTest` for suspend/use-case tests.
- Keep mocks strict and verify only meaningful interactions.

## Known project-specific patterns

- `NewExpenditureUseCase` creates `ExpenseCreationData` with `date = Date()`.
  - In tests, capture `ExpenseCreationData` and assert:
    - `expense`
    - `tripId`
    - `payerId`
    - `detail`
  - Do not assert direct object equality against a separately created `Date()`.

## Recommended command sequence

Use these in order when touching unit tests:

1. Targeted class:
   - `./gradlew :domain:testDebugUnitTest --tests com.anwera97.domain.NewExpenseUseCaseTest --no-daemon`
2. Affected module:
   - `./gradlew :domain:testDebugUnitTest --no-daemon`
3. Full unit-test baseline:
   - `./gradlew testDebugUnitTest --no-daemon --continue`

## PR checklist for unit-test edits

- Test names still reflect behavior.
- No flaky time-based assertions.
- Matchers/captors used correctly.
- Failing CI test reproduced locally.
- Full `testDebugUnitTest` passes before merge.

