plugins {
    `java-version-script`
    `mod-build-script`
}

dependencies {
    modImplementation(project(":${rootProject.name}-core"))
    modImplementation(project(":${rootProject.name}-igui"))
}
