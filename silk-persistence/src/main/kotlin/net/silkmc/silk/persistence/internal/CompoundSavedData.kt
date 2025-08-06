package net.silkmc.silk.persistence.internal

import com.mojang.serialization.Codec
import net.minecraft.nbt.CompoundTag
import net.minecraft.util.datafix.DataFixTypes
import net.minecraft.world.level.saveddata.SavedData
import net.minecraft.world.level.saveddata.SavedDataType
import net.silkmc.silk.core.annotations.InternalSilkApi
import net.silkmc.silk.persistence.PersistentCompound

@InternalSilkApi
class CompoundSavedData(val compound: PersistentCompound) : SavedData() {

    // a compound does not track whether it is dirty, however custom
    // logic is implemented via DimensionDataStorageMixin
    // to prevent file creation for empty compounds
    override fun isDirty(): Boolean {
        return true
    }

    companion object {

        private fun createCodec(levelCompound: PersistentCompound): Codec<CompoundSavedData> {
            return CompoundTag.CODEC.xmap({ nbtCompound ->
                levelCompound.loadFromCompound(nbtCompound, loadRaw = true)
                return@xmap CompoundSavedData(levelCompound)
            }, { savedData ->
                val nbtCompound = CompoundTag()
                savedData.compound.putInCompound(nbtCompound, writeRaw = true)
                return@xmap nbtCompound
            })
        }

        fun createType(id: String, levelCompound: PersistentCompound): SavedDataType<CompoundSavedData> {
            return SavedDataType(id, { CompoundSavedData(levelCompound) }, createCodec(levelCompound),
                /* dataFixType will be ignored on reading, but it cannot be null */ DataFixTypes.LEVEL)
        }

        internal val shouldBlockDataFixer: ThreadLocal<Boolean> = ThreadLocal.withInitial { false }
    }
}
