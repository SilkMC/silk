# FabrikMC

*Fabrik [[faˈbriːk]](https://cdn.duden.de/_media_/audio/ID4108073_10393000.mp3) (German for factory) is the place where fabrics and quilts are produced.*

[Discord](https://discord.gg/CJDUVuJ)

FabrikMC is an API for using FabricMC with Kotlin

It offers:

- an inventory GUI API
- a Kotlin wrapper for Brigardier
- a Kotlin DSL for creating complex `Text` objects
- coroutine utilities
- kotlinx.serialization support for Minecraft classes
- a scoreboard (sideboard) API
- `ItemStack` utils
- serialization of any class to NBT with kotlinx.serialization
- an NBT builder
- an API for storing persistent data on Chunks, Entities etc

This is not Fabric, this is just a Kotlin extension for Fabric - called Fabri<ins>k</ins>.

## Dependency

### Add it to your Gradle project

The core module:

```kotlin
// Kotlin DSL
modImplementation("net.axay:fabrikmc-core:version")
// Groovy DSL
modImplementation 'net.axay:fabrikmc-core:version'
```

#### Available modules

You can find all **available modules**, and the **current version**,
on [Maven Central](https://repo1.maven.org/maven2/net/axay/).

Currently, these are:
- fabrikmc-core
- fabrikmc-commands
- fabrikmc-igui
- fabrikmc-nbt
- fabrikmc-persistence

### Add it to your `fabric.mod.json`

Make sure that you mod correctly depends on FabrikMC, by adding it to the list of dependencies.

e.g.:

```json
{
  "depends": {
    "fabrikmc-core": "*",
    "fabrikmc-commands": "*",
    "fabrikmc-nbt": "*"
  },
  "suggests": {
    "fabrikmc-igui": "*",
    "fabrikmc-persistence": "*"
  }
}
```

### Provide it at runtime

*(recommended)* You can download the final mod (which you have to provide in
production) [on Modrinth](https://modrinth.com/mod/fabrik/versions).

To keep it simple, you can download the `-all` jar, which includes all modules.

*(alternative)* The mod can also be
downloaded [from Curseforge](https://www.curseforge.com/minecraft/mc-mods/fabrik/files).

Also, make sure that you
provide [fabric-language-kotlin](https://www.curseforge.com/minecraft/mc-mods/fabric-language-kotlin/files) and
the [fabric-api](https://www.curseforge.com/minecraft/mc-mods/fabric-api/files) at runtime.

### Lightweight and Stable

Fabrik does not put a whole new API on top of Mojang's code, instead it provides utilities which make high use of the
possibilites Kotlin gives you. You decide where to use Fabrik, Fabric or just the Vanilla Code. Additionally, Fabrik is
structured in modules, meaning that you can just take what you need.

Fabrik tries to use Minecraft functions and classes which are likely to stay the same / be stable.
