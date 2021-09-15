import BuildConstants.projectTitle

plugins {
    `java-version-script`
    `mod-build-script`
    `mod-publish-script`
    `kotest-script`
    `dokka-script`
}

dependencies {
    implementation(project(":${rootProject.name}-core"))
}

val modName by extra("$projectTitle Commands")
val modDepends by extra(linkedMapOf("${rootProject.name}-core" to "*"))
