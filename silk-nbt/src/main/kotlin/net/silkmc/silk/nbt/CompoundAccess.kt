@file:Suppress("FINAL_UPPER_BOUND", "unused")

package net.silkmc.silk.nbt

import net.minecraft.nbt.CompoundTag

operator fun CompoundTag.set(key: String, value: Boolean) = putBoolean(key, value)

operator fun CompoundTag.set(key: String, value: Byte) = putByte(key, value)
operator fun CompoundTag.set(key: String, value: Short) = putShort(key, value)
operator fun CompoundTag.set(key: String, value: Int) = putInt(key, value)
operator fun CompoundTag.set(key: String, value: Long) = putLong(key, value)

operator fun CompoundTag.set(key: String, value: Float) = putFloat(key, value)
operator fun CompoundTag.set(key: String, value: Double) = putDouble(key, value)

operator fun CompoundTag.set(key: String, value: String) = putString(key, value)

operator fun CompoundTag.set(key: String, value: ByteArray) = putByteArray(key, value)
operator fun CompoundTag.set(key: String, value: Array<Byte>) = putByteArray(key, value.toByteArray())
@JvmName("setByteList")
operator fun CompoundTag.set(key: String, value: List<Byte>) = putByteArray(key, value.toByteArray())

operator fun CompoundTag.set(key: String, value: IntArray) = putIntArray(key, value)
operator fun CompoundTag.set(key: String, value: Array<Int>) = putIntArray(key, value.toIntArray())
@JvmName("setIntList")
operator fun CompoundTag.set(key: String, value: List<Int>) = putIntArray(key, value.toIntArray())

operator fun CompoundTag.set(key: String, value: LongArray) = putLongArray(key, value)
operator fun CompoundTag.set(key: String, value: Array<Long>) = putLongArray(key, value.toLongArray())
@JvmName("setLongList")
operator fun CompoundTag.set(key: String, value: List<Long>) = putLongArray(key, value.toLongArray())
