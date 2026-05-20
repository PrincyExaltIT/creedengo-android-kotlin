plugins {
    alias(libs.plugins.kotlin.jvm)
    `java-library`
    alias(libs.plugins.shadow)
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

val sonarqubeMinVersion = libs.versions.sonarqube.plugin.api.impl.get()

dependencies {
    // SonarQube runtime — provided by the host server, do NOT bundle
    compileOnly(libs.sonarsource.kotlin.plugin)
    compileOnly(libs.sonarsource.plugin.api)

    // Bundled into our plugin JAR (server-side rule metadata + profile loaders)
    implementation(libs.sonarsource.analyzer.commons)

    // Tests — testkit needs the same provided deps available at compile time
    testImplementation(libs.sonarsource.plugin.api)
    testImplementation(libs.sonarsource.plugin.api.impl)
    testImplementation(libs.sonarsource.kotlin.plugin)
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.assertj.core)
    testRuntimeOnly(libs.junit.platform.launcher)
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
    "Plugin-RequirePlugins"       to "kotlin:3.0.1",
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
