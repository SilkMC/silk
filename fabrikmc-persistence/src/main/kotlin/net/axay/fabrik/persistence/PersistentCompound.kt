package net.axay.fabrik.persistence

import net.minecraft.nbt.NbtCompound

private const val CUSTOM_DATA_KEY = "fabrikmcData"

internal fun NbtCompound.getPersistentData() = getCompound(CUSTOM_DATA_KEY)

internal fun NbtCompound.putPersistentData(data: NbtCompound) {
    if (!data.isEmpty)
        put(CUSTOM_DATA_KEY, data)
}

