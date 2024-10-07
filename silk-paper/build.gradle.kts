import BuildConstants.paperMinecraftVersion

plugins {
    `kotlin-project-script`
    id("io.papermc.paperweight.userdev")
    id("xyz.jpenilla.run-paper")
}

allprojects {
    // This version must be a valid Silk version,
    // available in one of the repositories defined below.
    //
    // It does NOT have to match the most recent development version of Silk,
    // since Paper releases for new Minecraft versions
    // might come much later than Fabric releases.
    version = "1.11.0"
}

repositories {
    mavenCentral()
}

val extractTransitive by configurations.registering { isTransitive = true }
val includeInJar by configurations.registering { isTransitive = false }

dependencies {
    paperweight.paperDevBundle("${paperMinecraftVersion}-R0.1-SNAPSHOT")

    // include all regular silk modules in their dev jar form
    for (module in BuildConstants.commonModules) {
        if (project.version != rootProject.version) {
            val moduleDep = implementation("net.silkmc:silk-${module}:${project.version}:dev") {
                artifacts.removeIf { it.classifier != "dev" }
                assert(artifacts.isNotEmpty())
                exclude("net.silkmc") // exclude applies to transitive dependencies
            }
            includeInJar(moduleDep)
            extractTransitive(moduleDep)
        } else {
            includeInJar(implementation(project(":silk-${module}", configuration = "namedElements"))!!)
            extractTransitive(project(":silk-${module}"))
        }
    }
}

paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev
    .ReobfArtifactConfiguration.MOJANG_PRODUCTION

tasks {
    processResources {
        val props = mapOf(
            "description" to project.description,
            "version" to project.version,
            "mcVersion" to paperMinecraftVersion,
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
                .map { zipTree(it) }
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
