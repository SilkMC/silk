import BuildConstants.projectTitle

plugins {
    `java-version-script`
    `mod-build-script`
    `mod-publish-script`
    `dokka-script`
}

dependencies {
    implementation(modProject(":${rootProject.name}-core"))
}

val modName by extra("$projectTitle Game")
