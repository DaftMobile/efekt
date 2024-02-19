import dev.mokkery.verify.VerifyMode

plugins {
    kotlin("multiplatform")
    id("dev.mokkery")
}

val libs = extensions
    .getByType<VersionCatalogsExtension>()
    .named("libs")

mokkery {
    defaultVerifyMode.set(VerifyMode.exhaustiveOrder)
}

kotlin {
    explicitApi()
    applyDefaultHierarchyTemplate()

    jvm()
    js(IR) {
        browser()
        nodejs()
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    tvosX64()
    tvosArm64()
    tvosSimulatorArm64()

    watchosX64()
    watchosArm64()
    watchosSimulatorArm64()

    macosArm64()
    macosX64()

    mingwX64()

    linuxX64()
    linuxArm64()
}
