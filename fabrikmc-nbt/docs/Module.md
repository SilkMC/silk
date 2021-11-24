# Module fabrikmc-nbt

Provides [NBT](https://wiki.vg/NBT) serialization and deserialization using kotlinx.serialization. Additionally, this module contains some NBT
utilities, like simple conversion extensions.

${dependencyNotice}

## Serialization

You can serialize any class annotated with `@Serializable` to an [NbtElement][net.minecraft.nbt.NbtElement].

```kotlin
Nbt.encodeToNbtElement(value)
```

### Deserialization

In the same way it is possible deserialize any [NbtElement][net.minecraft.nbt.NbtElement] containing the correct entries
to a serializable class.

```kotlin
Nbt.decodeFromNbtElement(nbtElement)
```

### Configuration

You can configure the `Nbt` instance in the following way:

```kotlin
val nbt = Nbt {
    encodeDefaults = true // false by default
    ignoreUnknownKeys = false // true by default
}
```

## DSL

An NBT DSL is also available. The DSL supports normal key value pairs, compounds and nbt lists.

```kotlin
nbtCompound {
    put("x", 6)
    put("y", 21)
    put("name", "Peter")
    list("stringList", listOf("jo", "yes"))
    longArray("longSet", setOf(3L, 8L))
    compound("inner") {
        put("test", true)
    }
}
```

## Conversion

Extension functions to convert [Kotlin basic types](https://kotlinlang.org/docs/basic-types.html)
to [NbtElement][net.minecraft.nbt.NbtElement]s are available as well

```kotlin
1.toNbt(); true.toNbt(); IntArray(3) { it }.toNbt()
// etc
```

## Compound write access

You can modify an [NbtCompound][net.minecraft.nbt.NbtCompound] using the provided operator functions

```kotlin
nbtCompound["myint"] = 3
nbtCompound["coolboolean"] = false
```

# Package net.axay.fabrik.nbt

Contains `NbtCompound` access functions and conversion functions to create `NbtElement`s

# Package net.axay.fabrik.nbt.dsl

NBT builder DSL for `NbtCompound` and `NbtList`

# Package net.axay.fabrik.nbt.serialization

The main and public NBT serialization API

# Package net.axay.fabrik.nbt.serialization.decoder

Logic for decoding an NbtElement to a serializable class

# Package net.axay.fabrik.nbt.serialization.encoder

Logic for encoding a serializable class to an NbtElement
