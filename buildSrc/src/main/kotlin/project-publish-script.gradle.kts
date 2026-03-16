import BuildConstants.authors
import BuildConstants.githubRepo
import com.vanniktech.maven.publish.KotlinJvm

plugins {
    id("com.vanniktech.maven.publish")
}

mavenPublishing {
    configure(KotlinJvm())

    publishToMavenCentral(false)

    signAllPublications()

    pom {
        name = project.name
        description = project.description

        developers {
            authors.forEach {
                developer {
                    name = it
                }
            }
        }

        licenses {
            license {
                name = "GNU General Public License 3"
                url = "https://www.gnu.org/licenses/gpl-3.0.txt"
                distribution = "https://www.gnu.org/licenses/gpl-3.0.txt"
            }
        }

        url = "https://github.com/$githubRepo"

        scm {
            connection = "scm:git:git://github.com/$githubRepo.git"
            url = "https://github.com/$githubRepo/tree/main"
            developerConnection = "cm:git:ssh://git@github.com/$githubRepo.git"
        }
    }
}