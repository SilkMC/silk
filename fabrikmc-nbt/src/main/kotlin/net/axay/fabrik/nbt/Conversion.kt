package net.axay.fabrik.nbt

import net.minecraft.nbt.*

fun Boolean.toNbt(): NbtByte = NbtByte.of(this)
fun Byte.toNbt(): NbtByte = NbtByte.of(this)
fun Short.toNbt(): NbtShort = NbtShort.of(this)
fun Int.toNbt(): NbtInt = NbtInt.of(this)
fun Long.toNbt(): NbtLong = NbtLong.of(this)
fun Float.toNbt(): NbtFloat = NbtFloat.of(this)
fun Double.toNbt(): NbtDouble = NbtDouble.of(this)
fun Char.toNbt(): NbtInt = NbtInt.of(code)
fun String.toNbt(): NbtString = NbtString.of(this)

fun ByteArray.toNbt() = NbtByteArray(this)
fun List<Byte>.toNbt() = NbtByteArray(this)
fun IntArray.toNbt() = NbtIntArray(this)
fun List<Int>.toNbt() = NbtIntArray(this)
fun LongArray.toNbt() = NbtLongArray(this)
fun List<Long>.toNbt() = NbtLongArray(this)
