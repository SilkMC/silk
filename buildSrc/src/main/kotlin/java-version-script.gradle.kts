import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

val jvmVersion = JavaVersion.VERSION_1_8
val jvmVersionString = "1.8"

java {
    sourceCompatibility = jvmVersion
    targetCompatibility = jvmVersion
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = jvmVersionString
}
