import org.gradle.api.JavaVersion
import org.gradle.kotlin.dsl.withType

plugins {
    kotlin("jvm")
}

val jvmVersion = JavaVersion.VERSION_11
val jvmVersionString = "11"

java.sourceCompatibility = jvmVersion
java.targetCompatibility = jvmVersion

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = jvmVersionString
}
