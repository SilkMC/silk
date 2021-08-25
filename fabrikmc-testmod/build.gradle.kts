import BuildConstants.projectTitle

plugins {
    `java-version-script`
    `mod-build-script`
    kotlin("plugin.serialization") version "1.5.21"
}

dependencies {
    implementation(project(":${rootProject.name}-core"))
    implementation(project(":${rootProject.name}-igui"))
    implementation(project(":${rootProject.name}-commands"))
    implementation(project(":${rootProject.name}-persistence"))
}

val modName by extra("$projectTitle Testmod")
val modEntrypoints by extra(linkedMapOf("main" to listOf("net.axay.fabrik.test.ManagerKt::init")))
val modDepends by extra(
    linkedMapOf(
        "fabricloader" to ">=0.8.7"
    )
)
