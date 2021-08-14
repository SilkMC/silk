package net.axay.fabrik.persistence

import net.minecraft.nbt.NbtCompound

private const val CUSTOM_DATA_KEY = "fabrikmcData"

internal fun NbtCompound.getPersistentData() = getCompound(CUSTOM_DATA_KEY)

internal fun NbtCompound.putPersistentData(data: NbtCompound) {
    if (!data.isEmpty)
        put(CUSTOM_DATA_KEY, data)
}

operator fun NbtCompound.set(key: String, value: Boolean) = putBoolean(key, value)

operator fun NbtCompound.set(key: String, value: Byte) = putByte(key, value)
operator fun NbtCompound.set(key: String, value: Short) = putShort(key, value)
operator fun NbtCompound.set(key: String, value: Int) = putInt(key, value)
operator fun NbtCompound.set(key: String, value: Long) = putLong(key, value)

operator fun NbtCompound.set(key: String, value: Float) = putFloat(key, value)
operator fun NbtCompound.set(key: String, value: Double) = putDouble(key, value)

operator fun NbtCompound.set(key: String, value: String) = putString(key, value)

operator fun NbtCompound.set(key: String, value: ByteArray) = putByteArray(key, value)
operator fun NbtCompound.set(key: String, value: Array<Byte>) = putByteArray(key, value.toByteArray())
@JvmName("setByteList")
operator fun NbtCompound.set(key: String, value: List<Byte>) = putByteArray(key, value)

operator fun NbtCompound.set(key: String, value: IntArray) = putIntArray(key, value)
operator fun NbtCompound.set(key: String, value: Array<Int>) = putIntArray(key, value.toIntArray())
@JvmName("setIntList")
operator fun NbtCompound.set(key: String, value: List<Int>) = putIntArray(key, value)

operator fun NbtCompound.set(key: String, value: LongArray) = putLongArray(key, value)
operator fun NbtCompound.set(key: String, value: Array<Long>) = putLongArray(key, value.toLongArray())
@JvmName("setLongList")
operator fun NbtCompound.set(key: String, value: List<Long>) = putLongArray(key, value)
