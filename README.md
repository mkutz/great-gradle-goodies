# Great Gradle Goodies


## Code Formatting with [Spotless](https://github.com/diffplug/spotless/tree/main/plugin-gradle)

See [build.gradle.kts](build.gradle.kts)

- Automatic formatting for all source files in the project


## [Toolchains for JVM Projects](https://docs.gradle.org/current/userguide/toolchains.html) and [GitHub setup-java](https://github.com/marketplace/actions/setup-java-jdk)

See [services/toolchains](services/toolchains)

- [Toolchains](https://docs.gradle.org/current/userguide/toolchains.html) allow to declare the toolchain (javac, java, javadoc, etc.) for a project build.
- [GitHub setup-java](https://github.com/marketplace/actions/setup-java-jdk) allows to set up a specific JDK for the GitHub Actions workflow.
- Both should be in sync, but we don't want to declare versions redundantly across multiple [.github/workflows/*.yml](.github/workflows/) files and our [build.gradle.kts](build.gradle.kts) file(s).
- setup-java allows to [use a java-version-file](https://github.com/actions/setup-java/blob/main/docs/advanced-usage.md#Java-version-file) instead of a hardcoded version:

  ```yaml
  - uses: actions/setup-java@v4
    with:
      distribution: temurin
      java-version-file: services/toolchains/.java-version
  ```

- We can use the same file in the build.gradle.kts file(s) with a little hack:

  ```kotlin
  java {
    toolchain {
      languageVersion = JavaLanguageVersion
          .of(file(".java-version")
              .readText(Charsets.UTF_8)
              .trim())
      vendor = JvmVendorSpec.ADOPTIUM
    }
  }
  ```


## [Build Cache](https://docs.gradle.org/current/userguide/build_cache.html) & [GitHub setup-gradle](https://github.com/gradle/actions/tree/main/setup-gradle)

See [services/cache](services/cache)

- Build cache puts files into a cache directory (in the user's home dir) to be reused in subsequent builds.
- This is particularly useful for local builds when you only changed stuff in the test code (compiling the main code can be skipped).
- The setup-gradle action helps to set up the caching applied to Gradle builds… including the build cache!
- Combining both makes it possible to use the build cache across CI builds.
- Activate the build cache for your project by adding [gradle.properties](gradle.properties) to your project:

  ```properties
  org.gradle.caching=true
  ```

- Use it in your workflows:

  ```yaml
  - uses: gradle/actions/setup-gradle@v3 # manages the cache
  - run: ./gradlew check --no-daemon
  ```


## [Version Catalogs](https://docs.gradle.org/current/userguide/platforms.html#sub:central-declaration-of-dependencies)

See [services/version-catalogs](services/version-catalogs)

- Create a file [gradle/libs.versions.toml](gradle/libs.versions.toml) to declare all project dependencies in one central file.
- [Rich versions](https://docs.gradle.org/current/userguide/rich_versions.html#rich-version-constraints)
- [TOML files](https://toml.io/en/) Tom's Obvious, Minimal Language
- Reference dependencies form anywhere in the project via the global version catalog:

  ```kotlin
  plugins { 
    alias(libs.plugins.springBoot)
    // …
  }
  // …
  dependencies {
    implementation(libs.springBootStarterDataJpa)
    // …
  }
  ```


## [Test Suites](https://docs.gradle.org/current/userguide/jvm_test_suite_plugin.html)<sup>incubating</sup> & [Test Fixtures](https://docs.gradle.org/current/userguide/java_testing.html#sec:java_test_fixtures)

See [services/test-suites](services/test-suites)

- Allows to create different source sets with different dependencies for each and have shared code in an additional test fixtures source set.
- Also, it is possible to declare shared test dependencies for all the source sets (e.g. JUnit, AssertJ, Mockito, etc.).

```kotlin
plugins {
  // …
  `java-test-fixtures`
  `jvm-test-suite`
  // …
}

// …

testing {
  suites {
    // configure all test suites
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

    // configure the built-in unit test suite
    val test by
    getting(JvmTestSuite::class) {
      dependencies { implementation(libs.junitJupiterParams) }
    }

    // configure the integration test suite
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
```


## [Dependency Submission](https://github.com/gradle/actions/blob/main/docs/dependency-submission.md)

See [.github/workflows/submit-dependencies.yml](.github/workflows/submit-dependencies.yml)

- In addition to the setup-gradle action, there's submit-dependencies, which submits the dependency graph to GitHub.
- As a result we will see Gradle dependencies [in the GitHub UI](https://github.com/mkutz/great-gradle-goodies/security)!

  ```yaml
  name: Submit Dependencies

  on:
    workflow_dispatch:
    schedule:
      - cron: '0 9 * * 1-5'
    push:
      branches:
        - main
      paths:
        - '**/build.gradle.kts'

  jobs:
    build:
      permissions:
        contents: write

      runs-on: rd-ubuntu-latest

      steps:
        - uses: actions/checkout@v4

        - uses: actions/setup-java@v4
          with:
            distribution: temurin
            version: 21

        - uses: gradle/actions/dependency-submission@v4
  ```
