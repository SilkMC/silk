import BuildConstants.projectTitle

description = "Silk Commands provides a Kotlin DSL for Brigadier"

plugins {
    `kotlin-project-script`
    `mod-build-script`
    `project-publish-script`
    `kotest-script`
    `dokka-script`
}

dependencies {
    api(modProject(":${rootProject.name}-core"))
}

val modName by extra("$projectTitle Commands")
val modMixinFiles by extra(listOf("${rootProject.name}-commands.mixins.json"))
val modDepends by extra(linkedMapOf("${rootProject.name}-core" to "*"))
