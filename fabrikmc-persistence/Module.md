# Module fabrikmc-persistence

Allows you to persistently store any (serializable) data on Chunks, Entities (including Players), Worlds and more.

If the component is currently loaded, the data is kept in memory as well for faster access.

## Define a compound key

A compound key is required to write and write values to a persistent compound. These read and write operations can then
happend type-safe thanks to the compound key.

Such a key can be created in the following way:

```kt
val personKey = compoundKey<Person>(identifier)
```

## Retrieve a persistent compound

A compound can be retrieved from any compound provider, currently these are `Chunk` and `Entity`.

For easy access, use the `provider.persistentCompound` extension value.

```kt
with(chunk.persistentCompound) {
    it[personKey] = Person("John", 32, "France")
}
```

# Package net.axay.fabrik.persistence

Contains compound key creation and access to persistent compounds
