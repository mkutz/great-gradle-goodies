@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("org.springframework.boot") version "3.3.2"
  id("io.spring.dependency-management") version "1.1.6"
  val kotlinVersion = "2.0.0"
  id("org.jetbrains.kotlin.jvm") version kotlinVersion
  id("org.jetbrains.kotlin.plugin.spring") version kotlinVersion
  id("org.jetbrains.kotlin.plugin.jpa") version kotlinVersion
  id("io.gitlab.arturbosch.detekt") version "1.23.6"
}

repositories { mavenCentral() }

dependencies {
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("org.springframework.boot:spring-boot-starter-validation")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  runtimeOnly("org.postgresql:postgresql")

  val junitVersion = "5.10.2"
  testImplementation(platform("org.junit:junit-bom:$junitVersion"))
  testImplementation("org.junit.jupiter:junit-jupiter-api")
  testImplementation("org.junit.jupiter:junit-jupiter-params")
  val assertJVersion = "3.26.3"
  testImplementation("org.assertj:assertj-core:$assertJVersion")

  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("org.springframework.boot:spring-boot-testcontainers")
  testImplementation("org.testcontainers:junit-jupiter")
  testImplementation("org.testcontainers:postgresql")

  testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java { toolchain { languageVersion = JavaLanguageVersion.of(21) } }

tasks.withType<KotlinCompile> {
  kotlinOptions {
    freeCompilerArgs += "-Xjsr305=strict"
    freeCompilerArgs += "-Xemit-jvm-type-annotations"
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
  testLogging { events("passed", "skipped", "failed") }
}
