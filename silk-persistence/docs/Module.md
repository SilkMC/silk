# Module silk-persistence

Allows you to persistently store any (serializable) data on chunks, entities (including players), worlds and more.

If the component is currently loaded, the data is kept in memory as well for faster access.

${dependencyNotice}

## Define a compound key

A compound key is required to write and write values to a persistent compound. These read and write operations can then
happen type-safe thanks to the compound key.

Such a key can be created in the following way:

```kt
val personKey = compoundKey<Person>(identifier)
```

Note: `Person` must be serializable here

### Other compound key types

There are other compound key types:

- [nbtElementCompoundKey][net.axay.silk.persistence.nbtElementCompoundKey] for storing NbtElements, as they don't have
  to (and cannot) be serialized
- [customCompoundKey][net.axay.silk.persistence.customCompoundKey] for defining custom serialization logic (object
  to [NbtElement][net.minecraft.nbt.NbtElement]), for example if the class you are storing is not serializable, or if it
  is significantly faster than the built-in NBT serialization of silk-nbt

## Retrieve a persistent compound

A compound can be retrieved from any compound provider, currently these are [Chunk][net.minecraft.world.chunk.Chunk],
[Entity][net.minecraft.entity.Entity] and [World][net.minecraft.world.World].

For easy access, use the `provider.persistentCompound` extension value:

```kt
chunk.persistentCompound
entity.persistentCompound
world.persistentCompound
```

## Use a persistent compound

### Thread safety

Currently, persistent compounds are not thread safe, you should only modify them on the main game / server thread.

### Write operations

You can write to a compound using the index operator.

```kt
with(chunk.persistentCompound) {
    it[personKey] = Person("John", 32, "France")
}
```

### Read operations

You can read from a compound using the index operator.

```kt
with(chunk.persistentCompound) {
    val person = it[personKey]
    // and then do something with it
    log.info(person.toString())
}
```

### More functions

Compounds offer more functions for more specific operations,
see [PersistentCompound][net.axay.silk.persistence.PersistentCompound] for a list of these functions.

# Package net.axay.silk.persistence

Contains compound key creation and access to persistent compounds
