package net.silkmc.silk.persistence.internal

import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.datafix.DataFixTypes
import net.minecraft.world.level.saveddata.SavedData
import net.silkmc.silk.core.annotations.InternalSilkApi
import net.silkmc.silk.persistence.PersistentCompound

@InternalSilkApi
class CompoundSavedData(val compound: PersistentCompound) : SavedData() {

    // a compound does not track whether it is dirty, however custom
    // logic is implemented via a mixin and EmptySilkPersistenceCompoundTag
    // to prevent file creation for empty compounds
    override fun isDirty(): Boolean {
        return true
    }

    override fun save(nbt: CompoundTag, provider: HolderLookup.Provider): CompoundTag {
        compound.putInCompound(nbt, writeRaw = true)
        return if (nbt.isEmpty) EmptySilkPersistenceCompoundTag else nbt
    }


    companion object {

        internal val shouldBlockDataFixer: ThreadLocal<Boolean> = ThreadLocal.withInitial { false }

        fun load(nbt: CompoundTag, targetCompound: PersistentCompound) =
            CompoundSavedData(targetCompound.apply { loadFromCompound(nbt, loadRaw = true) })

        @JvmStatic
        fun createFactory(compound: PersistentCompound) =
            Factory(
                { CompoundSavedData(compound) },
                { nbt, _ -> load(nbt, compound) },
                DataFixTypes.LEVEL // not actually, but we cannot use null here
            )
    }
}
