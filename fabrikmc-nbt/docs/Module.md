# Module fabrikmc-nbt

Provides NBT serialization and deserialization using kotlinx.serialization. Additionally, this module contains some NBT utilities, like simple
conversion extensions.

${dependencyNotice}

## Serialization and deserialization

You can serialize any class annotated with `@Serializable` to an `NbtElement`. In the same way it is possible
deserialize any `NbtElement` containing the correct entries to a serializable class.

```kt
Nbt.encodeToNbtElement(value)
Nbt.decodeFromNbtElement(nbtElement)
```

## Configuration

You can configure the `Nbt` instance in the following way:

```kt
val nbt = Nbt {
    encodeDefaults = true // false by default
    ignoreUnknownKeys = false // true by default
}
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
