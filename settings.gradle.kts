
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "efekt"

include(":efekt-core")
include(":efekt-data")
include(":efekt-datetime")
include(":efekt-test")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}
