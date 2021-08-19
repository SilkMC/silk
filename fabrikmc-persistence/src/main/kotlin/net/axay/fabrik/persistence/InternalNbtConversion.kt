package net.axay.fabrik.persistence

import net.axay.fabrik.nbt.Nbt
import net.axay.fabrik.nbt.encodeToNbtElement
import net.axay.fabrik.nbt.toNbt
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtList

@Suppress("UNCHECKED_CAST")
internal inline fun <reified T> T.toPrimitiveNbtOrNull() = when (T::class) {
    Boolean::class -> (this as Boolean).toNbt()
    Byte::class -> (this as Byte).toNbt()
    Short::class -> (this as Short).toNbt()
    Int::class -> (this as Int).toNbt()
    Long::class -> (this as Long).toNbt()
    Float::class -> (this as Float).toNbt()
    Double::class -> (this as Double).toNbt()
    Char::class -> (this as Char).toNbt()
    String::class -> (this as String).toNbt()

    ByteArray::class -> (this as ByteArray).toNbt()
    IntArray::class -> (this as IntArray).toNbt()
    LongArray::class -> (this as LongArray).toNbt()

    Array<Byte>::class -> (this as Array<Byte>).toByteArray().toNbt()
    Array<Int>::class -> (this as Array<Int>).toIntArray().toNbt()
    Array<Long>::class -> (this as Array<Long>).toLongArray().toNbt()

    else -> null
}

internal inline fun <reified T> T.convertOrSerializeToNbt() = toPrimitiveNbtOrNull()
    ?: Nbt.encodeToNbtElement(this)

@Suppress("UNCHECKED_CAST")
internal inline fun <reified T> Collection<T>.toNbt(): NbtElement {
    return when (T::class) {
        Byte::class -> (this as Collection<Byte>).toByteArray().toNbt()
        Int::class -> (this as Collection<Int>).toIntArray().toNbt()
        Long::class -> (this as Collection<Long>).toLongArray().toNbt()
        else -> {
            val list = NbtList()
            forEach { list.add(it.convertOrSerializeToNbt()) }
            list
        }
    }
}
