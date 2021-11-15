import gradle.kotlin.dsl.accessors._a819bbffcdb721ad180221de4a453285.kotlin
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

kotlin.sourceSets.all {
    languageSettings.optIn("kotlin.RequiresOptIn")
}
