package net.silkmc.silk.nbt

import net.minecraft.nbt.*
import java.util.*

fun Boolean.toNbt(): ByteTag = ByteTag.valueOf(this)
fun Byte.toNbt(): ByteTag = ByteTag.valueOf(this)
fun Short.toNbt(): ShortTag = ShortTag.valueOf(this)
fun Int.toNbt(): IntTag = IntTag.valueOf(this)
fun Long.toNbt(): LongTag = LongTag.valueOf(this)
fun Float.toNbt(): FloatTag = FloatTag.valueOf(this)
fun Double.toNbt(): DoubleTag = DoubleTag.valueOf(this)
fun Char.toNbt(): IntTag = IntTag.valueOf(code)
fun String.toNbt(): StringTag = StringTag.valueOf(this)

fun ByteArray.toNbt() = ByteArrayTag(this)
fun List<Byte>.toNbt() = ByteArrayTag(this)
fun IntArray.toNbt() = IntArrayTag(this)
fun List<Int>.toNbt() = IntArrayTag(this)
fun LongArray.toNbt() = LongArrayTag(this)
fun List<Long>.toNbt() = LongArrayTag(this)
fun <T : Tag> List<T>.toNbt() = ListTag().also { it.addAll(this) }

fun UUID.toNbt(): IntArrayTag = NbtUtils.createUUID(this)
