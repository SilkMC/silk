import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

tasks {
    withType<JavaCompile> {
        options.apply {
            release.set(16)
            encoding = "UTF-8"
        }
    }

    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "16"
    }
}
