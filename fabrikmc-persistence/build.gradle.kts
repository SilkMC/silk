plugins {
    `java-version-script`
    `mod-build-script`
    `mod-publish-script`
}

dependencies {
    implementation(project(":${rootProject.name}-core"))
    implementation(project(":${rootProject.name}-nbt"))
}
