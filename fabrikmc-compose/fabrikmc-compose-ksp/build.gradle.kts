plugins {
    `java-version-script`
    `dokka-script-noop`
    kotlin("plugin.serialization")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.devtools.ksp:symbol-processing-api:1.6.10-1.0.2")
    implementation("com.squareup:kotlinpoet-ksp:1.10.2")
    implementation(project(":${rootProject.name}-compose:${rootProject.name}-compose-mojang-api"))
}
