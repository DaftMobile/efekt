import dev.mokkery.verify.VerifyMode

plugins {
    id("efekt-mutliplatform")
}
mokkery {
    defaultVerifyMode.set(VerifyMode.exhaustiveOrder)
}
dependencies {
    commonMainApi(libs.kotlinx.coroutines.core)
    commonMainApi(libs.turbine)
}
