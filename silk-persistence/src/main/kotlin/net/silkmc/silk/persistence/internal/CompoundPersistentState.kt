package net.silkmc.silk.persistence.internal

import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.world.level.saveddata.SavedData
import net.silkmc.silk.persistence.PersistentCompound

class CompoundPersistentState(private val compound: PersistentCompound) : SavedData() {

    override fun save(nbt: CompoundTag, provider: HolderLookup.Provider) =
        nbt.also { compound.putInCompound(it, true) }

    companion object {

        val BLOCK_DATA_FIXER: ThreadLocal<Boolean> = ThreadLocal.withInitial { false }

        fun load(nbt: CompoundTag, targetCompound: PersistentCompound) =
            CompoundPersistentState(targetCompound.apply { loadFromCompound(nbt, true) })
    }
}
