plugins {
    id("efekt-publish")
    id("efekt-mutliplatform")
}

dependencies {
    commonMainApi(project(":efekt-core"))
    commonMainApi(libs.turbine)
    commonMainImplementation(kotlin("test"))
    commonTestImplementation(libs.kotlinx.coroutines.test)
    commonTestImplementation(libs.kotest.assertions.core)
}
