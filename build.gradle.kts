plugins {
    kotlin("jvm") version "2.0.21"
}

group = "io.creedengo"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(17)
}
