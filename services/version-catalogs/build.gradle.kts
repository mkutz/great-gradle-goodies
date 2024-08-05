@file:Suppress("UnstableApiUsage")

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

kotlin {
  compilerOptions {
    freeCompilerArgs = listOf("-Xjsr305=strict", "-Xemit-jvm-type-annotations")
    allWarningsAsErrors = true
  }
}

tasks.withType<Test> {
  useJUnitPlatform()
  testLogging { events("passed", "skipped", "failed") }
}

configurations
  .matching { it.name == "detekt" }
  .all {
    resolutionStrategy.eachDependency {
      if (requested.group == "org.jetbrains.kotlin") {
        useVersion(io.gitlab.arturbosch.detekt.getSupportedKotlinVersion())
      }
    }
  }
