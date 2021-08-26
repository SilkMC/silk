# Module fabrikmc-nbt

Provides NBT serialization and deserialization using kotlinx.serialization. This allows you to serialize any class
annotated with `@Serializable` to an `NbtElement`. Additionally, this module contains some NBT utilities, like simple
conversion extensions.

## Serialization and deserialization

You can serialize any class to a compound

```kt
Nbt.decodeFromNbtElement<TestClass>(compound)
```
