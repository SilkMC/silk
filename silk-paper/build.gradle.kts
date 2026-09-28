import BuildConstants.majorMinecraftVersion
import org.objectweb.asm.*
import java.io.InputStream
import java.util.zip.ZipFile

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
            val deps =
                extractTransitive.get().resolvedConfiguration.firstLevelModuleDependencies.flatMap { it.children }
                    .apply { assert(any { it.name == "fabric-language-kotlin" }) }
                    .flatMap { flDep -> flDep.children.map { it.module } }.filterNot { it.id.group == "net.fabricmc" }
                    .mapTo(LinkedHashSet()) { it.toString() }
            depsFile.writeText(deps.joinToString("\n"))
        }
    }

    jar {
        dependsOn(includeInJar)
        from({
            includeInJar.get().filter { it.name.endsWith("jar") }.map(::zipTree)
        }) {
            filesMatching(
                listOf("fabric.mod.json", "*.mixins.json", "*-refmap.json")
            ) {
                exclude()
            }
            duplicatesStrategy = DuplicatesStrategy.FAIL
        }
    }

    val checkPaperEvents = register("checkEventImplementations") {
        group = "verification"
        description = "Checks wheater all Silk Events are implemented in paper."
        val eventJars = files(includeInJar)
        val paperClasses = sourceSets.main.get().output.classesDirs
        val excluded = setOf(
            "CommandEvents.registerClient",
            //any event which is not called on serverside
        )
        val rootDir = rootProject.projectDir
        inputs.files(eventJars, paperClasses)

        doLast {
            val eventTypes = setOf("Lnet/silkmc/silk/core/event/Event;", "Lnet/silkmc/silk/core/event/AsyncEvent;")

            val events = HashMap<String, Pair<String, String>>()
            val referenced = HashSet<String>()

            fun readClass(bytes: ByteArray, collectEvents: Boolean = false) {
                ClassReader(bytes).accept(object : ClassVisitor(Opcodes.ASM9) {
                    lateinit var owner: String

                    override fun visit(
                        version: Int,
                        access: Int,
                        name: String,
                        signature: String?,
                        superName: String?,
                        interfaces: Array<out String>?,
                    ) {
                        owner = name
                    }

                    override fun visitMethod(
                        access: Int,
                        name: String,
                        descriptor: String,
                        signature: String?,
                        exceptions: Array<out String>?,
                    ): MethodVisitor? {
                        if (collectEvents) {
                            if (name.startsWith("get") && descriptor.removePrefix("()") in eventTypes) {
                                val eventName = owner.substringAfterLast('/') + "." + name.removePrefix("get")
                                    .replaceFirstChar(Char::lowercase)

                                val source = rootDir
                                    .listFiles { it.name.startsWith("silk-") }
                                    .orEmpty()
                                    .map { it.resolve("src/main/kotlin/$owner.kt") }
                                    .firstOrNull(File::exists)

                                val getter = "$owner.$name"
                                return object : MethodVisitor(Opcodes.ASM9) {
                                    var firstLine = 0
                                    override fun visitLineNumber(line: Int, start: Label) {
                                        if (firstLine == 0) firstLine = line
                                    }

                                    override fun visitEnd() {
                                        events[getter] = eventName to (source?.let {
                                            "file://${it.absolutePath}:${
                                                firstLine.coerceAtLeast(1)
                                            }:1 "
                                        } ?: "")
                                    }
                                }
                            }
                            return null
                        }

                        return object : MethodVisitor(Opcodes.ASM9) {
                            override fun visitMethodInsn(
                                opcode: Int,
                                owner: String,
                                name: String,
                                descriptor: String,
                                isInterface: Boolean,
                            ) {
                                referenced += "$owner.$name"
                            }
                        }
                    }
                }, ClassReader.SKIP_FRAMES)
            }

            eventJars.filter { it.name.endsWith("jar") }.forEach { jar ->
                ZipFile(jar).use { zip ->
                    zip
                        .entries()
                        .asSequence()
                        .filter { it.name.endsWith(".class") }
                        .map(zip::getInputStream)
                        .map(InputStream::readBytes)
                        .forEach { bytes -> readClass(bytes, true) }
                }
            }

            paperClasses
                .asFileTree.matching { include("**/*.class") }
                .map(File::readBytes)
                .forEach(::readClass)

            val missing = events
                .filterKeys { it !in referenced }
                .values.filterNot { it.first in excluded }
                .sortedBy { it.first }

            missing.forEach { (event, location) ->
                logger.warn("w: ${location}Silk event $event is not implemented on Paper")
            }
        }
    }

    check {
        dependsOn(checkPaperEvents)
    }
}
