@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  alias(libs.plugins.springBoot)
  alias(libs.plugins.springDependencyManagement)
  alias(libs.plugins.kotlinJvm)
  alias(libs.plugins.kotlinSpring)
  alias(libs.plugins.kotlinJpa)
  alias(libs.plugins.detekt)
}

repositories { mavenCentral() }

dependencies {
  implementation(libs.springBootStarterDataJpa)
  implementation(libs.springBootStarterWebflux)
  implementation(libs.springBootStarterValidation)
  implementation(libs.jacksonModuleKotlin)
  runtimeOnly(libs.postgresql)

  testImplementation(platform(libs.junitBom))
  testImplementation(libs.junitJupiterApi)
  testImplementation(libs.junitJupiterParams)
  testImplementation(libs.assertjCore)

  testImplementation(libs.springBootStarterTest)
  testImplementation(libs.springBootTestcontainers)
  testImplementation(libs.testcontainersJunitJupiter)
  testImplementation(libs.testcontainersPostgresql)

  testRuntimeOnly(libs.junitPlatformLauncher)
}

java {
  toolchain {
    languageVersion = JavaLanguageVersion.of(file(".java-version").readText(Charsets.UTF_8).trim())
  }
}

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
