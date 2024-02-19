plugins {
    id("efekt-mutliplatform")
}

dependencies {
    commonMainApi(project(":efekt-core"))
    commonMainApi(libs.kotlinx.datetime)
    commonTestImplementation(kotlin("test"))
    commonTestImplementation(libs.kotest.assertions.core)
}

