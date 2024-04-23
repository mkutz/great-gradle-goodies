rootProject.name = "great-gradle-goodies"

include("services:base")

include("services:cache")

include("services:toolchains")

include("services:version-catalogs")

include("services:test-suites")

System.setProperty("sonar.gradle.skipCompile", "true")
