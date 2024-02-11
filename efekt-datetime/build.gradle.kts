import dev.mokkery.verify.VerifyMode

plugins {
    id("efekt-mutliplatform")
}
mokkery {
    defaultVerifyMode.set(VerifyMode.exhaustiveOrder)
}
dependencies {
    commonMainApi(project(":efekt-core"))
    commonMainApi(libs.kotlinx.datetime)
    commonTestImplementation(kotlin("test"))
    commonTestImplementation(libs.kotest.assertions.core)
}

