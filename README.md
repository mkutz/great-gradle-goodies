# Great Gradle Goodies
[![Build](https://github.com/mkutz/great-gradle-goodies/actions/workflows/build.yml/badge.svg)](https://github.com/mkutz/great-gradle-goodies/actions/workflows/build.yml)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=mkutz_great-gradle-goodies&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=mkutz_great-gradle-goodies)


## [Toolchains for JVM Projects](https://docs.gradle.org/current/userguide/toolchains.html) and [GitHub setup-java](https://github.com/marketplace/actions/setup-java-jdk)

- [Toolchains](https://docs.gradle.org/current/userguide/toolchains.html) allow to declare the toolchain (javac, java, javadoc, etc.) for a project build.
- [GitHub setup-java](https://github.com/marketplace/actions/setup-java-jdk) allows to set up a specific JDK for the GitHub Actions workflow.
- Both should be in sync, but we don't want to declare versions redundantly across multiple [.github/workflows/*.yml](.github/workflows/) files and our [build.gradle.kts](build.gradle.kts) file(s).
- setup-java allows to [use a java-version-file](https://github.com/actions/setup-java/blob/main/docs/advanced-usage.md#Java-version-file) instead of a hardcoded version:

  ```yaml
  - uses: actions/setup-java@v2
    with:
      java-version-file: .java-version
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


## [Version Catalogs](https://docs.gradle.org/current/userguide/platforms.html#sub:central-declaration-of-dependencies)

- Create a file [libs.versions.toml](libs.versions.toml) to declare all project dependencies in one central file.
- [Rich versions](https://docs.gradle.org/current/userguide/rich_versions.html#rich-version-constraints)
- [TOML files](https://toml.io/en/) Tom's Obvious, Minimal Language

TODO


## [Test Suites](https://docs.gradle.org/current/userguide/jvm_test_suite_plugin.html)<sup>incubating</sup> & [Test Fixtures](https://docs.gradle.org/current/userguide/java_testing.html#sec:java_test_fixtures)

- Allows to create different source sets with different dependencies for each and have shared code in an additional test fixtures source set.
- Also, it is possible to declare shared test dependencies for all the source sets (e.g. JUnit, AssertJ, Mockito, etc.).

TODO


## [Build Cache](https://docs.gradle.org/current/userguide/build_cache.html) & [GitHub setup-gradle](https://github.com/gradle/actions/tree/main/setup-gradle)

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


## [Dependency Submission](https://github.com/gradle/actions/blob/main/docs/dependency-submission.md)

- In addition to the setup-gradle action, there's submit-dependencies, which submits the dependency graph to GitHub.
- As a result we will see Gradle dependencies in the GitHub UI!

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
            overwrite-settings: false

        - uses: gradle/actions/dependency-submission@v3
  ```
