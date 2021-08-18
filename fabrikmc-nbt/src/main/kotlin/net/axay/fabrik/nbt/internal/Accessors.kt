package net.axay.fabrik.nbt.internal

import net.axay.fabrik.nbt.mixin.NbtCompoundAccessor
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement

private val entriesField = NbtCompound::class.java
    .getDeclaredField("entries")
    .apply { isAccessible = true }

// In environments where mixins are not applied (unit tests) we fall back to reflection
internal val NbtCompound.entries: Map<String, NbtElement>
    @Suppress("unchecked_cast")
    get() = (this as? NbtCompoundAccessor)?.entries
        ?: entriesField.get(this) as Map<String, NbtElement>
