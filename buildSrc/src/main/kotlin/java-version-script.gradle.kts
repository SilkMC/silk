import org.gradle.api.JavaVersion
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

val jvmVersion = JavaVersion.VERSION_11
val jvmVersionString = "11"

java.sourceCompatibility = jvmVersion
java.targetCompatibility = jvmVersion

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = jvmVersionString
}
