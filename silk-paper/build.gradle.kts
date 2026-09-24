import BuildConstants.majorMinecraftVersion

plugins {
    `kotlin-project-script`
    id("io.papermc.paperweight.userdev")
    id("xyz.jpenilla.run-paper")
}

repositories {
    mavenCentral()
}

val extractTransitive = configurations.register("extractTransitive") { isTransitive = true }
val includeInJar = configurations.register("includeInJar") { isTransitive = false }

dependencies {
    paperweight.paperDevBundle("$majorMinecraftVersion.build.+")

    // include all regular silk modules in their dev jar form
    for (module in BuildConstants.uploadModules) {
        includeInJar(implementation(project(":silk-${module}"))!!)
        extractTransitive(project(":silk-${module}"))
    }
}

tasks {
    processResources {
        val props = mapOf(
            "description" to project.description,
            "version" to project.version,
            "mcVersion" to majorMinecraftVersion,
        )
        inputs.properties(props)
        filesMatching("paper-plugin.yml") {
            expand(props)
        }

        val depsFile = destinationDir.resolve("silkDependencies.txt")
        outputs.file(depsFile)

        // extract relevant transitive dependencies, specifically from fabric-language-kotlin
        doLast {
            val deps = extractTransitive.get().resolvedConfiguration
                .firstLevelModuleDependencies.flatMap { it.children }
                .apply { assert(any { it.name == "fabric-language-kotlin" }) }
                .flatMap { flDep -> flDep.children.map { it.module } }
                .filterNot { it.id.group == "net.fabricmc" }
                .mapTo(LinkedHashSet()) { it.toString() }
            depsFile.writeText(deps.joinToString("\n"))
        }
    }

    jar {
        dependsOn(includeInJar)
        from({
            includeInJar.get()
                .filter { it.name.endsWith("jar") }
                .map(::zipTree)
        }) {
            filesMatching(
                listOf("fabric.mod.json", "*.mixins.json", "*-refmap.json")
            ) {
                exclude()
            }
            duplicatesStrategy = DuplicatesStrategy.FAIL
        }
    }
}
