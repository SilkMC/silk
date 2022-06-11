import BuildConstants.projectTitle

description = "Silk Game contains utilities not fitting for the core module which may be useful for minigames"

plugins {
    `kotlin-project-script`
    `mod-build-script`
    `project-publish-script`
    `dokka-script`
}

dependencies {
    api(modProject(":${rootProject.name}-core"))
}

val modName by extra("$projectTitle Game")
