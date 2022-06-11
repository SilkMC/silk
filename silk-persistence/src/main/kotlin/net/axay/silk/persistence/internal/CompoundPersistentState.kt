package net.axay.silk.persistence.internal

import net.axay.silk.persistence.PersistentCompound
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.saveddata.SavedData

class CompoundPersistentState(private val compound: PersistentCompound) : SavedData() {
    override fun save(nbt: CompoundTag) =
        nbt.also { compound.putInCompound(it, true) }

    companion object {
        fun load(nbt: CompoundTag, targetCompound: PersistentCompound) =
            CompoundPersistentState(targetCompound.apply { loadFromCompound(nbt, true) })
    }
}
