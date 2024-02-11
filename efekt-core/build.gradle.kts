import dev.mokkery.verify.VerifyMode

plugins {
    id("efekt-mutliplatform")
}
mokkery {
    defaultVerifyMode.set(VerifyMode.exhaustiveOrder)
}
dependencies {
    commonMainApi(libs.kotlinx.coroutines.core)
    commonTestImplementation(kotlin("test"))
    commonTestImplementation(libs.kotlinx.coroutines.test)
    commonTestImplementation(libs.kotest.assertions.core)
    commonTestImplementation(libs.turbine)
}
