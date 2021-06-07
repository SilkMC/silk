# FabrikMC

[Discord](https://discord.gg/CJDUVuJ)

FabrikMC is an API for using FabricMC with Kotlin

It offers:

- an inventory GUI API
- a Kotlin wrapper for Brigardier
- a Kotlin DSL for creating complex `Text` objects
- coroutine utilities
- kotlinx.serialization support
- a scoreboard (sideboard) API
- `ItemStack` utils

This is not Fabric, this is just a Kotlin extension for Fabric - called Fabri<ins>k</ins>.

Fabrik does not put a whole new API on top of Mojang's code, instead it provides utilities which make high use of the
possibilites Kotlin gives you. You decide where to use Fabrik, Fabric or just the Vanilla Code. Additionally, Fabrik is
structured in modules, meaning that you can just take what you need.

## Dependency

### Add it to your Gradle project

The core module:

Gradle _Kotlin DSL_

```kotlin
modImplementation("net.axay:fabrikmc-core:version")
```

Gradle _Groovy DSL_

```groovy
modImplementation 'net.axay:fabrikmc-core:version'
```

You can find all **available modules**, and the **current version**,
on [Maven Central](https://repo1.maven.org/maven2/net/axay/).

### Add it to your `fabric.mod.json`

Make sure that you mod correctly depends on FabrikMC, by adding it to the list of dependencies.

e.g.:

```json
{
  "depends": {
    ...
    "fabrikmc-core": "*",
    "fabrikmc-commands": "*"
  },
  "suggests": {
    "fabrikmc-igui": "*"
  }
}
```

### Provide it at runtime

You can download the final mod (which you have to provide in
production) [on CurseForge](https://www.curseforge.com/minecraft/mc-mods/fabrikmc/files).

Also, make sure that you
provide [fabric-language-kotlin](https://www.curseforge.com/minecraft/mc-mods/fabric-language-kotlin/files) and
the [fabric-api](https://www.curseforge.com/minecraft/mc-mods/fabric-api/files) at runtime.
