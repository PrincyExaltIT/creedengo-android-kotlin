# syntax=docker/dockerfile:1.7

# --- Stage 1: build the shadow jar ---
FROM eclipse-temurin:17-jdk AS builder
WORKDIR /src
COPY gradle ./gradle
COPY gradlew settings.gradle.kts build.gradle.kts ./
RUN sed -i 's/\r$//' gradlew && chmod +x gradlew && ./gradlew --version
COPY src ./src
ARG PLUGIN_VERSION=0.0.1-SNAPSHOT
RUN ./gradlew --no-daemon -Pversion=${PLUGIN_VERSION} shadowJar

# --- Stage 2: SonarQube + plugin ---
FROM sonarqube:25.12.0.117093-community
COPY --from=builder /src/build/libs/creedengo-android-kotlin-*.jar \
     /opt/sonarqube/extensions/plugins/
