
plugins {
    kotlin("multiplatform")
    id("dev.mokkery")
}

val libs = extensions
    .getByType<VersionCatalogsExtension>()
    .named("libs")

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

    sourceSets {

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.findLibrary("kotlinx-coroutines-test").get())
            implementation(libs.findLibrary("kotest-assertions-core").get())
        }
    }
}
