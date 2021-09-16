import BuildConstants.fabricApiVersion
import BuildConstants.yarnMappingsVersion
import java.net.URL

plugins {
    id("org.jetbrains.dokka")
}

tasks {
    val processDokkaMarkdown = register<Copy>("processDokkaMarkdown") {
        from(layout.projectDirectory.dir("docs"))
        into(layout.buildDirectory.dir("docs-markdown"))

        val properties = linkedMapOf(
            "dependencyNotice" to """
                ### Dependency
                ```kt
                modImplementation("net.axay:${project.name}:${rootProject.version}")
                ```
            """.trimIndent()
        )

        inputs.properties(properties)
        expand(properties)
    }

    withType<org.jetbrains.dokka.gradle.DokkaTaskPartial> {
        dependsOn(processDokkaMarkdown)

        dokkaSourceSets {
            configureEach {
                includes.from(
                    buildDir.resolve("docs-markdown").listFiles()!!
                        .sortedBy { if (it.name == "Module.md") "0" else it.name }
                        .map { "build/docs-markdown/${it.name}" }
                        .toTypedArray()
                )

                sourceLink {
                    localDirectory.set(file("src/main/kotlin"))
                    remoteUrl.set(
                        URL(
                            "https://github.com/jakobkmar/fabrikmc/tree/main/${project.name}/src/main/kotlin"
                        )
                    )
                    remoteLineSuffix.set("#L")
                }

                listOf("internal", "mixin").forEach {
                    perPackageOption {
                        matchingRegex.set(""".*\.$it.*""")
                        suppress.set(true)
                    }
                }

                externalDocumentationLink {
                    val yarnJavadocVersion = yarnMappingsVersion.removeSuffix(":v2")
                    url.set(URL("https://maven.fabricmc.net/docs/yarn-$yarnJavadocVersion/"))
                    packageListUrl.set(URL("https://maven.fabricmc.net/docs/yarn-$yarnJavadocVersion/element-list"))
                }

                externalDocumentationLink {
                    url.set(URL("https://maven.fabricmc.net/docs/fabric-api-$fabricApiVersion/"))
                    packageListUrl.set(URL("https://maven.fabricmc.net/docs/fabric-api-$fabricApiVersion/element-list"))
                }
            }
        }
    }
}
