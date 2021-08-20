import BuildConstants.fabrikVersion
import BuildConstants.githubRepo
import BuildConstants.isSnapshot

plugins {
    kotlin("jvm")

    id("fabric-loom")

    `maven-publish`
    signing
}

publishing {
    repositories {
        maven {
            name = "ossrh"
            credentials(PasswordCredentials::class)
            setUrl(
                if (!isSnapshot)
                    "https://oss.sonatype.org/service/local/staging/deploy/maven2"
                else
                    "https://oss.sonatype.org/content/repositories/snapshots"
            )
        }
    }

    publications {
        register<MavenPublication>(project.name) {
            val remapJarTask = tasks.named("remapJar")
            artifact(remapJarTask) {
                builtBy(remapJarTask)
            }
            artifact(tasks.named("sourcesJar")) {
                builtBy(tasks.named("remapSourcesJar"))
            }
            artifact(tasks.named("javadocJar"))
            artifact(tasks.jar)

            this.groupId = project.group.toString()
            this.artifactId = project.name
            this.version = fabrikVersion

            pom {
                name.set(project.name)
                description.set(project.description)

                developers {
                    developer {
                        name.set("bluefireoly")
                    }
                }

                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }

                url.set("https://github.com/${githubRepo}")

                scm {
                    connection.set("scm:git:git://github.com/${githubRepo}.git")
                    url.set("https://github.com/${githubRepo}/tree/main")
                }
            }
        }
    }
}

signing {
    sign(publishing.publications)
}
