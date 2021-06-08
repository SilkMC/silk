import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

tasks {
    withType<JavaCompile> {
        options.release.set(16)
    }

    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "16"
    }
}
