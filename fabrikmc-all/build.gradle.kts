import BuildConstants.projectTitle

plugins {
    `java-version-script`
    `mod-build-script`
    `mod-upload-script`
}

dependencies {
    include(modProject(":${rootProject.name}-commands"))
    include(modProject(":${rootProject.name}-compose"))
    include(modProject(":${rootProject.name}-core"))
    include(modProject(":${rootProject.name}-game"))
    include(modProject(":${rootProject.name}-igui"))
    include(modProject(":${rootProject.name}-nbt"))
    include(modProject(":${rootProject.name}-network"))
    include(modProject(":${rootProject.name}-persistence"))
}

val modName by extra("$projectTitle (All modules)")
