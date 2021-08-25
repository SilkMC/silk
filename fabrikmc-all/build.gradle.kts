import BuildConstants.projectTitle

plugins {
    `java-version-script`
    `mod-build-script`
    `mod-upload-script`
}

dependencies {
    include(project(":${rootProject.name}-commands"))
    include(project(":${rootProject.name}-core"))
    include(project(":${rootProject.name}-igui"))
    include(project(":${rootProject.name}-nbt"))
    include(project(":${rootProject.name}-persistence"))
}

val modName by extra("$projectTitle (All modules)")
