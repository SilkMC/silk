import BuildConstants.authors
import BuildConstants.githubRepo
import BuildConstants.isSnapshot

plugins {
    kotlin("jvm")

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
            from(components["java"])

            this.groupId = project.group.toString()
            this.artifactId = project.name
            this.version = rootProject.version.toString()

            pom {
                name.set(project.name)
                description.set(project.description)

                developers {
                    authors.forEach {
                        developer {
                            name.set(it)
                        }
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
