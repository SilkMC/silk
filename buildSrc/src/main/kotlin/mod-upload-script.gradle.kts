import BuildConstants.curseforgeId
import BuildConstants.majorMinecraftVersion
import BuildConstants.minecraftVersion
import BuildConstants.modrinthId
import BuildConstants.projectState
import BuildConstants.projectStateType
import com.matthewprenger.cursegradle.CurseProject
import com.matthewprenger.cursegradle.CurseRelation
import com.matthewprenger.cursegradle.Options
import com.modrinth.minotaur.dependencies.DependencyType
import com.modrinth.minotaur.dependencies.ModDependency

plugins {
    kotlin("jvm")

    id("fabric-loom")
    id("com.matthewprenger.cursegradle")
    id("com.modrinth.minotaur")
}

val fabrikModules = listOf(
    project(":${rootProject.name}-commands"),
    project(":${rootProject.name}-core"),
    project(":${rootProject.name}-game"),
    project(":${rootProject.name}-igui"),
    project(":${rootProject.name}-nbt"),
    project(":${rootProject.name}-network"),
    project(":${rootProject.name}-persistence")
)

tasks {
    named("curseforge") {
        onlyIf {
            findProperty("curseforge.token") != null
        }
        dependsOn(tasks.named("remapJar"))
    }
}

modrinth {
    token.set(findProperty("modrinth.token").toString())

    projectId.set(modrinthId)
    versionNumber.set(rootProject.version.toString())
    versionType.set(projectStateType.name)
    gameVersions.set(listOf(minecraftVersion))
    loaders.set(listOf("fabric"))

    uploadFile.set(tasks.remapJar.get())

    dependencies.set(
        listOf(
            ModDependency("P7dR8mSH", DependencyType.REQUIRED),
            ModDependency("Ha28R6CL", DependencyType.REQUIRED),
        )
    )
}

curseforge {
    apiKey = findProperty("curseforge.token").toString()

    project(closureOf<CurseProject> {
        mainArtifact(tasks.named("remapJar").get())

        id = curseforgeId
        releaseType = projectState
        addGameVersion(if (minecraftVersion.split('-').size <= 1) minecraftVersion else "$majorMinecraftVersion-Snapshot")

        relations(closureOf<CurseRelation> {
            requiredDependency("fabric-api")
            requiredDependency("fabric-language-kotlin")
        })
    })
    options(closureOf<Options> {
        forgeGradleIntegration = false
    })
}
