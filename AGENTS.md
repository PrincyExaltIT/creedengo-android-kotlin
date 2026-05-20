# AGENTS.md

## Project scope

This repository contains the Creedengo SonarQube plugin for Android Kotlin.

Primary goals when working here:

- keep changes focused on Android Kotlin eco-design rules
- preserve SonarQube plugin compatibility and packaging
- prefer small, verifiable changes over broad refactors

## Key project areas

- `src/main/kotlin/io/creedengo/android/kotlin`: plugin entry point, rule definition, quality profile, and rule implementations
- `src/main/resources/io/creedengo/android/kotlin`: rule metadata and built-in profile resources
- `src/test/kotlin/io/creedengo/android/kotlin`: unit tests for plugin wiring and checks
- `docs/CONTRIBUTE.md`: detailed contributor guide for local setup and architecture

## Working rules for agents

1. Keep edits minimal and aligned with the existing Kotlin and Gradle style.
2. Do not change unrelated rules, metadata, or packaging while implementing a targeted fix.
3. When adding or changing a rule, update all relevant parts together:
   - implementation under `src/main/kotlin`
   - metadata under `src/main/resources/.../rules`
   - `AndroidKotlinRulesDefinition` rule key list when needed
   - `creedengo_way_profile.json` when the rule belongs in the built-in profile
   - unit tests under `src/test/kotlin`
4. Prefer syntax-based PSI checks when they are sufficiently robust; use semantic matching when method ownership or type resolution matters.
5. Keep issue messages and rule behavior consistent with the eco-design intent of the existing rules.

## Validation expectations

Use the narrowest validation that fits the change:

- `./gradlew test` for rule or plugin wiring changes
- `./gradlew build` when packaging may be affected
- `./gradlew shadowJar` before restarting the local SonarQube container

For local end-to-end validation:

1. Build the plugin JAR.
2. Start SonarQube with `docker compose up -d`.
3. Restart `sonar` after rebuilding the plugin.
4. Verify that the repository `creedengo-android-kotlin` and profile `creedengo way` are visible in SonarQube.

## Environment assumptions

- JDK 17
- Gradle wrapper from this repository
- Docker Compose for local SonarQube and PostgreSQL

## Reference documents

- `README.md` for project overview and quickstart
- `CONTRIBUTING.md` for shared contribution guidance
- `docs/CONTRIBUTE.md` for repository-specific contributor documentation
- `CODE_STYLE.md` for code style guidance inherited from the common project
