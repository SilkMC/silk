plugins {
    `java-version-script`
    `mod-build-script`
    `mod-publish-script`
}

dependencies {
    modImplementation(project(":${rootProject.name}-core"))
}
