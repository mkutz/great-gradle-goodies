plugins {
  id("org.sonarqube") version "7.0.1.6134"
  id("com.diffplug.spotless") version "6.25.0"
}

repositories { mavenCentral() }

sonar {
  properties {
    property("sonar.projectKey", "mkutz_great-gradle-goodies")
    property("sonar.organization", "mkutz")
    property("sonar.host.url", "https://sonarcloud.io")
  }
}

spotless {
  kotlin {
    target("**/*.kt")
    targetExclude("**/build/**/*.gradle.kts")
    ktfmt().googleStyle()
  }

  kotlinGradle {
    target("**/*.gradle.kts")
    targetExclude("**/build/**/*.gradle.kts")
    ktfmt().googleStyle()
  }

  yaml {
    target("**/*.yml")
    jackson()
  }
}
