@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
  implementation("org.springframework.boot:spring-boot-starter-data-jpa")
  implementation("org.springframework.boot:spring-boot-starter-webflux")
  implementation("org.springframework.boot:spring-boot-starter-validation")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  runtimeOnly("org.postgresql:postgresql")
}

testing {
  suites {
    withType(JvmTestSuite::class).configureEach {
      useJUnitJupiter()
      dependencies {
        implementation(platform(libs.junitBom))
        implementation("org.junit.jupiter:junit-jupiter-api")
        implementation(libs.assertjCore)
        runtimeOnly("org.junit.platform:junit-platform-launcher")
      }
    }

    val test by
      getting(JvmTestSuite::class) {
        dependencies { implementation("org.junit.jupiter:junit-jupiter-params") }
      }

    register<JvmTestSuite>("integrationTest") {
      dependencies {
        implementation(project())
        implementation(testFixtures(project()))
        implementation("org.springframework.boot:spring-boot-starter-test")
        implementation("org.springframework.boot:spring-boot-starter-webflux")
        implementation("org.springframework.boot:spring-boot-starter-data-jpa")
        implementation("org.springframework.boot:spring-boot-testcontainers")
        implementation("org.testcontainers:junit-jupiter")
        implementation("org.testcontainers:postgresql")
      }
      testType = TestSuiteType.INTEGRATION_TEST
      targets { all { testTask.configure { shouldRunAfter(test) } } }
    }
  }
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

tasks.withType<Test> { useJUnitPlatform() }
