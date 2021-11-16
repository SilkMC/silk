import BuildConstants.projectTitle

description = "FabrikMC Game contains utilities not fitting for the core module which may be useful for minigames"

plugins {
    `java-version-script`
    `mod-build-script`
    `project-publish-script`
    `dokka-script`
}

dependencies {
    api(modProject(":${rootProject.name}-core"))
}

val modName by extra("$projectTitle Game")
