plugins {
    `java-version-script`
    `mod-build-script`
}

dependencies {
    implementation(project(":${rootProject.name}-core"))
    implementation(project(":${rootProject.name}-igui"))
}
