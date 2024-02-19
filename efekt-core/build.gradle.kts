plugins {
    id("efekt-mutliplatform")
}

dependencies {
    commonMainApi(libs.kotlinx.coroutines.core)
    commonTestImplementation(kotlin("test"))
    commonTestImplementation(libs.kotlinx.coroutines.test)
    commonTestImplementation(libs.kotest.assertions.core)
    commonTestImplementation(libs.turbine)
}
