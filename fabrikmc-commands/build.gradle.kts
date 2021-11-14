import BuildConstants.projectTitle

description = "FabrikMC Commands provides a Kotlin DSL for Brigardier"

plugins {
    `java-version-script`
    `mod-build-script`
    `mod-publish-script`
    `kotest-script`
    `dokka-script`
}

dependencies {
    api(modProject(":${rootProject.name}-core"))
}

val modName by extra("$projectTitle Commands")
val modDepends by extra(linkedMapOf("${rootProject.name}-core" to "*"))
