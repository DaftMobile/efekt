import dev.mokkery.verify.VerifyMode

plugins {
    id("efekt-mutliplatform")
}

mokkery {
    defaultVerifyMode.set(VerifyMode.exhaustiveOrder)
}

dependencies {
    commonMainApi(project(":efekt-core"))
    commonMainApi(libs.turbine)
    commonMainImplementation(kotlin("test"))
    commonTestImplementation(libs.kotlinx.coroutines.test)
    commonTestImplementation(libs.kotest.assertions.core)
}
