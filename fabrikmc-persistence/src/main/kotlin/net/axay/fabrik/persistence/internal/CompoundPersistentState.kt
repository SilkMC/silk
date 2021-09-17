package net.axay.fabrik.persistence.internal

import net.axay.fabrik.persistence.PersistentCompound
import net.minecraft.nbt.NbtCompound
import net.minecraft.world.PersistentState

class CompoundPersistentState(private val compound: PersistentCompound) : PersistentState() {
    override fun writeNbt(nbt: NbtCompound) =
        nbt.also { compound.putInCompound(it, true) }

    companion object {
        fun fromNbt(nbt: NbtCompound, targetCompound: PersistentCompound) =
            CompoundPersistentState(targetCompound.apply { loadFromCompound(nbt, true) })
    }
}
