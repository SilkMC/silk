plugins {
    `java-version-script`
    `mod-build-script`
    `mod-publish-script`
    `kotest-script`
    kotlin("plugin.serialization") version "1.5.21"
}

loom {
    accessWidener = file("src/main/resources/fabrikmc-nbt.accesswidener")
}
