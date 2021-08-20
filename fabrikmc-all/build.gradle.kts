plugins {
    `java-version-script`
    `mod-build-script`
    `mod-publish-script`
}

dependencies {
    include(project(":${rootProject.name}-commands"))
    include(project(":${rootProject.name}-core"))
    include(project(":${rootProject.name}-igui"))
    include(project(":${rootProject.name}-nbt"))
    include(project(":${rootProject.name}-persistence"))
}
