import BuildConstants.curseforgeId
import BuildConstants.fabrikVersion
import BuildConstants.githubRepo
import BuildConstants.minecraftVersion
import BuildConstants.projectState
import com.matthewprenger.cursegradle.CurseProject
import com.matthewprenger.cursegradle.CurseUploadTask
import com.matthewprenger.cursegradle.Options
import net.fabricmc.loom.task.RemapJarTask

plugins {
    kotlin("jvm")

    id("fabric-loom")
    id("com.matthewprenger.cursegradle")

    `maven-publish`
    signing
}

val curseTasks = tasks.withType<CurseUploadTask> {
    dependsOn(tasks.withType<RemapJarTask>())
}

tasks.create("publishAndUploadMod") {
    group = "upload"
    dependsOn(curseTasks)
    dependsOn(tasks.getByName("publish"))
}

curseforge {
    apiKey = property("curseforge.token") ?: ""
    project(closureOf<CurseProject> {
        mainArtifact(tasks.getByName("remapJar").outputs.files.first())

        id = curseforgeId

        releaseType = projectState
        addGameVersion(minecraftVersion)
    })
    options(closureOf<Options> {
        forgeGradleIntegration = false
    })
}

publishing {
    repositories {
        maven("https://oss.sonatype.org/service/local/staging/deploy/maven2") {
            credentials {
                username = property("ossrh.username") as String
                password = property("ossrh.password") as String
            }
        }
    }

    publications {
        create<MavenPublication>(project.name) {
            val remapJarTask = tasks.named("remapJar").get()
            artifact(remapJarTask) {
                builtBy(remapJarTask)
            }
            artifact(tasks.getByName("sourcesJar"))
            artifact(tasks.getByName("javadocJar"))

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
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
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
