plugins {
    `java-version-script`
    `dokka-script-noop`
    kotlin("plugin.serialization")
}

repositories {
    mavenCentral()
}

apply(from = "${rootDir}/shared.properties.gradle.kts")
val kspVersion: String by extra

dependencies {
    implementation("com.google.devtools.ksp:symbol-processing-api:$kspVersion")
    implementation("com.squareup:kotlinpoet-ksp:1.10.2")
    implementation(project(":${rootProject.name}-compose:${rootProject.name}-compose-mojang-api"))
    implementation("org.slf4j:slf4j-simple:1.7.36")
}
