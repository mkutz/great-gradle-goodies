@file:Suppress("UnstableApiUsage")

plugins {
  alias(libs.plugins.springBoot)
  alias(libs.plugins.springDependencyManagement)
  alias(libs.plugins.kotlinJvm)
  alias(libs.plugins.kotlinSpring)
  alias(libs.plugins.kotlinJpa)
  alias(libs.plugins.detekt)
  `java-test-fixtures`
  `jvm-test-suite`
}

repositories { mavenCentral() }

dependencies {
  implementation(libs.springBootStarterDataJpa)
  implementation(libs.springBootStarterWebflux)
  implementation(libs.springBootStarterValidation)
  implementation(libs.jacksonModuleKotlin)
  runtimeOnly(libs.postgresql)
}

testing {
  suites {
    withType<JvmTestSuite> {
      useJUnitJupiter()
      dependencies {
        implementation(platform(libs.junitBom))
        implementation(libs.junitJupiterApi)
        implementation(libs.assertjCore)
        runtimeOnly(libs.junitPlatformLauncher)
      }
      targets {
        all { testTask.configure { testLogging { events("passed", "skipped", "failed") } } }
      }
    }

    val test by
      getting(JvmTestSuite::class) { dependencies { implementation(libs.junitJupiterParams) } }

    val integrationTest by
      registering(JvmTestSuite::class) {
        dependencies {
          implementation(testFixtures(project()))
          implementation(libs.springBootStarterTest)
          implementation(libs.springBootStarterWebflux)
          implementation(libs.springBootStarterDataJpa)
          implementation(libs.springBootTestcontainers)
          implementation(libs.testcontainersJunitJupiter)
          implementation(libs.testcontainersPostgresql)
        }
        targets { all { testTask.configure { shouldRunAfter(test) } } }
      }

    tasks.check { dependsOn(integrationTest) }
  }
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

configurations
  .matching { it.name == "detekt" }
  .all {
    resolutionStrategy.eachDependency {
      if (requested.group == "org.jetbrains.kotlin") {
        useVersion(io.gitlab.arturbosch.detekt.getSupportedKotlinVersion())
      }
    }
  }
