plugins {
    id("org.jetbrains.dokka")
}

tasks {
    withType<org.jetbrains.dokka.gradle.DokkaTaskPartial> {
        enabled = false
    }
}
