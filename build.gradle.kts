plugins {
  id("org.sonarqube") version "6.2.0.5505"
  id("com.diffplug.spotless") version "7.2.1"
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
