import BuildConstants.projectTitle

description = "Silk Testmod provides examples usages of Silk features"

plugins {
    `kotlin-project-script`
    `mod-build-script`
    kotlin("plugin.serialization")
}

dependencies {
    implementation(modProject(":${rootProject.name}-commands"))
    implementation(modProject(":${rootProject.name}-core"))
    implementation(modProject(":${rootProject.name}-game"))
    implementation(modProject(":${rootProject.name}-igui"))
    implementation(modProject(":${rootProject.name}-network"))
    implementation(modProject(":${rootProject.name}-persistence"))
}

val modName by extra("$projectTitle Testmod")
val modEntrypoints by extra(linkedMapOf(
    "main" to listOf("net.silkmc.silk.test.Manager"),
    "client" to listOf("net.silkmc.silk.test.Manager"),
))
val modDepends by extra(
    linkedMapOf(
        "fabricloader" to ">=0.8.7"
    )
)

idea {
    module {
        excludeDirs.add(file("run"))
    }
}
