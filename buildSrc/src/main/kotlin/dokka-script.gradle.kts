import java.net.URL

plugins {
    id("org.jetbrains.dokka")
}

tasks {
    withType<org.jetbrains.dokka.gradle.DokkaTaskPartial> {
        dokkaSourceSets {
            configureEach {
                includes.from("Module.md")

                sourceLink {
                    localDirectory.set(file("src/main/kotlin"))
                    remoteUrl.set(URL(
                        "https://github.com/jakobkmar/fabrikmc/tree/main/${project.name}/src/main/kotlin"
                    ))
                    remoteLineSuffix.set("#L")
                }
            }
        }
    }
}
