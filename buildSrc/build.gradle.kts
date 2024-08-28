plugins {
    `kotlin-dsl`
}

dependencies {
    api(libs.kotlin.plugin)
    api(libs.mokkery.plugin)
    api(libs.dokka.plugin)
    api(libs.vanniktech.publish.plugin)
}
