import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

val jvmVersion = JavaVersion.VERSION_11
val jvmVersionString = "11"

java {
    sourceCompatibility = jvmVersion
    targetCompatibility = jvmVersion
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = jvmVersionString
}
