package net.silkmc.silk.persistence.internal

import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.saveddata.SavedData
import net.silkmc.silk.persistence.PersistentCompound

class CompoundPersistentState(private val compound: PersistentCompound) : SavedData() {
    override fun save(nbt: CompoundTag) =
        nbt.also { compound.putInCompound(it, true) }

    companion object {
        fun load(nbt: CompoundTag, targetCompound: PersistentCompound) =
            CompoundPersistentState(targetCompound.apply { loadFromCompound(nbt, true) })
    }
}
