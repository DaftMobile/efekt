import dev.mokkery.verify.VerifyMode

plugins {
    id("efekt-mutliplatform")
}

dependencies {
    commonMainApi(project(":efekt-core"))
    commonTestImplementation(kotlin("test"))
    commonTestImplementation(libs.kotlinx.coroutines.test)
    commonTestImplementation(libs.kotest.assertions.core)
}

