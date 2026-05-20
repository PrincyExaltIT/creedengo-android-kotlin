# Contributing to Creedengo Android Kotlin

This document focuses on the Kotlin SonarQube plugin itself: what it does, how to run it locally, and how its implementation is structured.

## What the Sonar plugin does

Creedengo Android Kotlin is a SonarQube plugin dedicated to eco-design and energy-smell detection in Android Kotlin code.

In practice, the plugin adds three things to a SonarQube server:

- a Kotlin rule repository named `creedengo-android-kotlin`
- a built-in quality profile named `creedengo way`
- rule metadata and rule implementations that report Android-specific eco-design issues

The current rule set lives under `src/main/resources/io/creedengo/android/kotlin/rules` for metadata and under `src/main/kotlin/io/creedengo/android/kotlin/checks` for implementations. The rules currently present in this module include examples such as:

- `GCI505`: keeping the screen on through `Window.addFlags(...)`
- `GCI522`: forcing screen brightness to its maximum value
- `GCI600`: clearing application or filesystem cache programmatically

The plugin is packaged as a server-side JAR and mounted into SonarQube through Docker during local development. Once loaded by SonarQube, it contributes the Creedengo rule repository and its default Kotlin profile to the platform.

## Local environment for testing

### Prerequisites

Use the same baseline as the project build:

- JDK 17
- Docker Desktop or a compatible Docker Engine with Compose support
- a local SonarQube token if you want to run authenticated scans from a scanner

The Gradle build is configured with a Java 17 toolchain, and the Docker stack starts SonarQube Community plus PostgreSQL.

### Build the plugin

From the project root:

```bash
cd /Users/a422gq/Documents/sonar/creedengo-android-kotlin
./gradlew clean test shadowJar
```

This produces the plugin artifact at `build/libs/creedengo-android-kotlin-0.0.1-SNAPSHOT.jar`.

Useful validation commands during development:

```bash
./gradlew test
./gradlew build
```

`test` is the fastest feedback loop for unit-level validation, while `build` also verifies packaging.

### Start a local SonarQube with the plugin mounted

The Docker Compose file bind-mounts the generated JAR into SonarQube's plugin directory. After building the JAR, start the stack:

```bash
docker compose up -d
docker compose logs -f sonar
```

Wait until SonarQube reports that it is operational, then open `http://localhost:9000`.

Default credentials are:

- login: `admin`
- password: `admin`

When you change plugin code, rebuild and restart SonarQube:

```bash
./gradlew shadowJar
docker compose restart sonar
```

To stop the environment:

```bash
docker compose down
```

To fully reset the local database and volumes:

```bash
docker compose down -v
```

### Verify that the plugin is loaded

After SonarQube starts:

1. Open the Rules page and search for the repository `creedengo-android-kotlin`.
2. Open the Quality Profiles page and check that the built-in profile `creedengo way` exists for Kotlin.
3. Confirm that the rules declared in the repository are visible with their HTML and JSON metadata.

If the repository or profile does not appear, rebuild the shaded JAR first, then restart the `sonar` service.

### Run an end-to-end analysis locally

The most reliable way to test a rule end to end is to analyze a small Android Kotlin project containing both compliant and non-compliant examples.

Typical workflow:

1. Start the local SonarQube instance with the plugin.
2. Create or reuse a small Kotlin Android sample project.
3. Add one file that should trigger the rule and one file that should not.
4. Run a scan against `http://localhost:9000`.
5. Check that the issue appears in SonarQube with the expected rule key and message.

If you have `sonar-scanner` installed, a minimal scan command looks like this:

```bash
sonar-scanner \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.token=<your-token>
```

The workspace also contains smoke projects with `sonar-project.properties` files under sibling folders, but for Kotlin rule development you will usually want a sample that actually contains Android Kotlin sources exercising the rule under development.

## Key architecture

The module is intentionally small. The main pieces are:

### 1. Plugin entry point

`AndroidKotlinPlugin` is the SonarQube entry point. It registers the extensions that define the rule repository and the built-in quality profile.

### 2. Rule repository and profile metadata

`AndroidKotlinRulesDefinition` declares:

- the language as `kotlin`
- the repository key `creedengo-android-kotlin`
- the repository display name
- the list of supported rule keys

It loads rule metadata from JSON and HTML resources using Sonar analyzer commons.

`AndroidKotlinProfile` loads the built-in profile from `creedengo_way_profile.json`.

This separation is important:

- rule metadata answers what SonarQube should display
- rule implementation answers how issues are detected

### 3. Rule implementations

Rule code lives in `src/main/kotlin/io/creedengo/android/kotlin/checks`.

The existing rules show two implementation styles:

- syntax-oriented checks extending `AbstractCheck`
- semantic call-oriented checks extending `CallAbstractCheck`

Examples:

- `BrightnessOverrideRule` inspects assignment expressions directly
- `ClearCacheCheck` inspects call expressions and uses lightweight textual filtering
- `KeepScreenOnAddFlagsRule` inherits shared flag-analysis logic from `FlagOnMethodCheck`

### 4. Shared helpers

Helpers such as `FlagOnMethodCheck` centralize reusable detection logic. This keeps individual rules small and makes it easier to add more Android flag-related checks without duplicating argument parsing and constant handling.

### 5. Packaging

The plugin is built as a shaded JAR through the Shadow plugin. The Gradle manifest adds the SonarQube plugin metadata required by the server, including the plugin key, class name, required language, minimum Java version, and SonarQube compatibility information.

## How the AST works here

### Kotlin PSI first

This plugin works on Kotlin PSI nodes exposed through the Sonar Kotlin API. In concrete terms, rules inspect Kotlin structures such as:

- `KtCallExpression`
- `KtBinaryExpression`
- `KtDotQualifiedExpression`
- `KtNameReferenceExpression`

This is different from a simple line-based or regex-based analyzer: rules operate on parsed Kotlin syntax trees.

### Mixed syntax and semantic approach

The current codebase mixes two levels of analysis:

- direct PSI inspection for straightforward patterns
- semantic matching for method calls when type information matters

For example, `FlagOnMethodCheck` uses `FunMatcher` and `KaFunctionCall` so a rule can target a method such as `android.view.Window.addFlags` rather than relying only on the method name text. This is the safer approach when different APIs may share the same method name.

By contrast, `BrightnessOverrideRule` and parts of `ClearCacheCheck` are intentionally simpler and mostly syntactic. That keeps rules easy to understand and fast to iterate on, but it can be less precise than a fully semantic check.

### Compared to similar plugins

Compared to Java Sonar plugins:

- the traversal model is Kotlin PSI based, not Java AST based
- semantic call handling uses the Sonar Kotlin API and Kotlin analysis types such as `KaFunctionCall`
- rule code tends to work with Kotlin-specific constructs such as dot-qualified expressions and infix operators

Compared to Detekt or compiler-plugin style analyzers:

- the goal here is SonarQube issue reporting, repository metadata, and quality profile integration
- issues are reported through `KotlinFileContext`, not Detekt findings or compiler diagnostics
- packaging is a SonarQube server plugin, not a build-only linter extension

The practical consequence is that contributors should think in two layers:

- detection logic over Kotlin PSI and optional semantic resolution
- SonarQube integration through rule metadata, profile wiring, packaging, and server-side validation

## Suggested contribution workflow

For a new rule, the usual flow is:

1. Add the rule implementation under `src/main/kotlin/.../checks`.
2. Add the rule metadata files under `src/main/resources/io/creedengo/android/kotlin/rules`.
3. Register the rule key in `AndroidKotlinRulesDefinition` and, if appropriate, in `creedengo_way_profile.json`.
4. Add or update unit tests.
5. Run `./gradlew test`.
6. Build the JAR and validate the rule in the local SonarQube instance.

When choosing between a syntax-only rule and a semantic rule, prefer the smallest approach that is still robust enough to avoid obvious false positives.
