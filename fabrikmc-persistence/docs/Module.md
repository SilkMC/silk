# Module fabrikmc-persistence

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

- [net.axay.fabrik.persistence.nbtElementCompoundKey] for storing NbtElements, as they don't have to (and cannot) be serialized
- [net.axay.fabrik.persistence.customCompoundKey] for defining custom serialization logic (object to `NbtElement`), for example if the class you are storing is not serializable, or if it is significantly faster than the built in NBT serialization of fabrikmc-nbt

## Retrieve a persistent compound

A compound can be retrieved from any compound provider, currently these are `Chunk`, `Entity` and `World`.

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

YOu can write to a compound using the index operator.

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

Compounds offer more functions for more specific operations, see [net.axay.fabrik.persistence.PersistentCompound] for a
list of these functions.

# Package net.axay.fabrik.persistence

Contains compound key creation and access to persistent compounds
