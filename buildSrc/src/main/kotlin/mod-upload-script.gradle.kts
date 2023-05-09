import BuildConstants.minecraftVersion
import BuildConstants.modrinthId
import BuildConstants.projectState
import com.modrinth.minotaur.dependencies.DependencyType
import com.modrinth.minotaur.dependencies.ModDependency

plugins {
    kotlin("jvm")

    id("fabric-loom")
    id("com.modrinth.minotaur")
}

modrinth {
    token.set(findProperty("modrinth.token").toString())

    projectId.set(modrinthId)
    versionNumber.set(rootProject.version.toString())
    versionType.set(projectState)
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
