import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.withType

plugins {
    id("org.jetbrains.dokka")
}

tasks {
    withType<org.jetbrains.dokka.gradle.DokkaTaskPartial> {
        enabled = false
    }
}
