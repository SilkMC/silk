import BuildConstants.projectTitle

plugins {
    `java-version-script`
    `mod-build-script`
    `mod-publish-script`
}

dependencies {
    implementation(project(":${rootProject.name}-core"))
    api(project(":${rootProject.name}-nbt"))
}

val modName by extra("$projectTitle Persistence")
val modMixinFiles by extra(listOf("${rootProject.name}-persistence.mixins.json"))
val modDepends by extra(linkedMapOf("${rootProject.name}-core" to "*", "${rootProject.name}-nbt" to "*"))
