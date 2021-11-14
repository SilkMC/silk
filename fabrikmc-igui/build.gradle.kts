import BuildConstants.projectTitle

description = "FabrikMC Inventory GUI provides a high-level server-side GUI builder DSL for Kotlin"

plugins {
    `java-version-script`
    `mod-build-script`
    `mod-publish-script`
    `dokka-script`
}

dependencies {
    api(modProject(":${rootProject.name}-core"))
}

val modName by extra("$projectTitle Inventory GUI")
val modMixinFiles by extra(listOf("${rootProject.name}-igui.mixins.json"))
val modDepends by extra(linkedMapOf("${rootProject.name}-core" to "*"))
