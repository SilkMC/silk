import BuildConstants.projectTitle

plugins {
    `java-version-script`
    `mod-build-script`
    `mod-publish-script`
}

dependencies {
    implementation(project(":${rootProject.name}-core"))
}

val modName by extra("$projectTitle Inventory GUI")
val modMixinFiles by extra(listOf("${rootProject.name}-igui.mixins.json"))
val modDepends by extra(linkedMapOf("${rootProject.name}-core" to "*"))
