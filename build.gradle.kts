plugins {
    kotlin("jvm") version "2.0.21"
    `java-library`
    id("com.gradleup.shadow") version "8.3.6"
}

group = "io.creedengo"
version = "0.0.1-SNAPSHOT"

description = "Provides rules to reduce the environmental footprint of your Kotlin Android applications"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(17)
}

// --- Versions ---
val sonarPluginApiVersion = "9.8.0.203"
val sonarKotlinVersion = "3.0.1.6889"
val sonarAnalyzerCommonsVersion = "2.5.0.1358"
val sonarqubeMinVersion = "9.9.7.96285"

dependencies {
    // SonarQube runtime — provided by the host server, do NOT bundle
    compileOnly("org.sonarsource.kotlin:sonar-kotlin-plugin:$sonarKotlinVersion")
    compileOnly("org.sonarsource.api.plugin:sonar-plugin-api:$sonarPluginApiVersion")

    // Bundled into our plugin JAR (server-side rule metadata + profile loaders)
    implementation("org.sonarsource.analyzer-commons:sonar-analyzer-commons:$sonarAnalyzerCommonsVersion")

    // Tests
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    testImplementation("org.assertj:assertj-core:3.25.3")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

// SonarQube manifest entries — read by the server when loading the .jar
val pluginManifest = mapOf(
    "Plugin-Key"                  to "creedengoandroidkotlin",
    "Plugin-Class"                to "io.creedengo.android.kotlin.AndroidKotlinPlugin",
    "Plugin-Name"                 to "Creedengo Android Kotlin",
    "Plugin-Description"          to (project.description ?: ""),
    "Plugin-Version"              to version.toString(),
    "Plugin-Organization"         to "Green Code Initiative",
    "Plugin-OrganizationUrl"      to "https://github.com/green-code-initiative",
    "Plugin-Homepage"             to "https://github.com/green-code-initiative/creedengo-android-kotlin",
    "Plugin-IssueTrackerUrl"      to "https://github.com/green-code-initiative/creedengo-android-kotlin/issues",
    "Plugin-SourcesUrl"           to "https://github.com/green-code-initiative/creedengo-android-kotlin",
    "Plugin-License"              to "GPL v3",
    "Plugin-RequiredForLanguages" to "kotlin",
    "Sonar-Version"               to sonarqubeMinVersion,
    "SonarLint-Supported"         to "true",
    "Jre-Min-Version"             to "17"
)

tasks.shadowJar {
    archiveClassifier.set("")
    mergeServiceFiles()
    manifest { attributes(pluginManifest) }
}

// Replace the default thin jar with the shaded fat jar in the build output
tasks.jar { enabled = false }
tasks.build { dependsOn(tasks.shadowJar) }
