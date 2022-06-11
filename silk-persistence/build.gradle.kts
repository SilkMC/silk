import BuildConstants.projectTitle

description = "Silk Persistence adds class based persistent storage to entities, worlds and more"

plugins {
    `kotlin-project-script`
    `mod-build-script`
    `project-publish-script`
    `dokka-script`
}

dependencies {
    api(modProject(":${rootProject.name}-core"))
    api(modProject(":${rootProject.name}-nbt"))
}

val modName by extra("$projectTitle Persistence")
val modMixinFiles by extra(listOf("${rootProject.name}-persistence.mixins.json"))
val modDepends by extra(linkedMapOf("${rootProject.name}-core" to "*", "${rootProject.name}-nbt" to "*"))
