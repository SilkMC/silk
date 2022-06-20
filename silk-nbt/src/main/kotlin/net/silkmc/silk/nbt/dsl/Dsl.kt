package net.silkmc.silk.nbt.dsl

import net.silkmc.silk.nbt.toNbt
import net.minecraft.nbt.*

/**
 * Build an NBT compound.
 */
inline fun nbtCompound(build: NbtCompoundBuilder.() -> Unit) =
    NbtCompoundBuilder().apply(build).build()

/**
 * Build an NBT list.
 *
 * @see NbtListBuilder
 */
inline fun nbtList(build: NbtListBuilder.() -> Unit) =
    NbtListBuilder().apply(build).build()

class NbtCompoundBuilder {
    private val compound = CompoundTag()

    fun put(key: String, value: Tag) {
        compound.put(key, value)
    }

    fun put(key: String, value: Boolean) {
        put(key, value.toNbt())
    }

    fun put(key: String, value: Byte) {
        put(key, value.toNbt())
    }

    fun put(key: String, value: Short) {
        put(key, value.toNbt())
    }

    fun put(key: String, value: Int) {
        put(key, value.toNbt())
    }

    fun put(key: String, value: Long) {
        put(key, value.toNbt())
    }

    fun put(key: String, value: Float) {
        put(key, value.toNbt())
    }

    fun put(key: String, value: Double) {
        put(key, value.toNbt())
    }

    fun put(key: String, value: Char) {
        put(key, value.toNbt())
    }

    fun put(key: String, value: String) {
        put(key, value.toNbt())
    }

    inline fun compound(key: String, build: NbtCompoundBuilder.() -> Unit) {
        put(key, nbtCompound(build))
    }

    inline fun list(key: String, build: NbtListBuilder.() -> Unit) {
        put(key, nbtList(build))
    }

    /**
     * Puts an NBT list (*not* a primitive array) with all elements in `value`.
     *
     * @throws IllegalArgumentException if `T` not one of `Boolean`,
     * `Byte`, `Short`, `Int`, `Long`, `Float`, `Double`, `Char` or `String`.
     */
    inline fun <reified T> list(key: String, value: Iterable<T>) {
        when (T::class) {
            Boolean::class -> list(key) { value.forEach { add(it as Boolean) } }
            Byte::class -> list(key) { value.forEach { add(it as Byte) } }
            Short::class -> list(key) { value.forEach { add(it as Short) } }
            Int::class -> list(key) { value.forEach { add(it as Int) } }
            Long::class -> list(key) { value.forEach { add(it as Long) } }
            Float::class -> list(key) { value.forEach { add(it as Float) } }
            Double::class -> list(key) { value.forEach { add(it as Double) } }
            Char::class -> list(key) { value.forEach { add(it as Char) } }
            String::class -> list(key) { value.forEach { add(it as String) } }
            else -> throw IllegalArgumentException("Type ${T::class} is not a valid NBT type")
        }
    }

    fun byteArray(key: String, value: ByteArray) {
        put(key, value.toNbt())
    }

    fun byteArray(key: String, value: Collection<Byte>) =
        byteArray(key, value.toByteArray())

    fun intArray(key: String, value: IntArray) {
        put(key, value.toNbt())
    }

    fun intArray(key: String, value: Collection<Int>) =
        intArray(key, value.toIntArray())

    fun longArray(key: String, value: LongArray) {
        put(key, value.toNbt())
    }

    fun longArray(key: String, value: Collection<Long>) =
        longArray(key, value.toLongArray())

    fun build() = compound
}

/**
 * Builder class for an NBT list.
 *
 * [ListTag] determines its type from the first element added, all following
 * elements are required to have the same type, otherwise an
 * [UnsupportedOperationException] is thrown.
 */
class NbtListBuilder {
    private val list = ListTag()

    fun add(value: Tag) {
        list.add(value)
    }

    fun add(value: Boolean) {
        add(value.toNbt())
    }

    fun add(value: Byte) {
        add(value.toNbt())
    }

    fun add(value: Short) {
        add(value.toNbt())
    }

    fun add(value: Int) {
        add(value.toNbt())
    }

    fun add(value: Long) {
        add(value.toNbt())
    }

    fun add(value: Float) {
        add(value.toNbt())
    }

    fun add(value: Double) {
        add(value.toNbt())
    }

    fun add(value: Char) {
        add(value.toNbt())
    }

    fun add(value: String) {
        add(value.toNbt())
    }

    inline fun compound(build: NbtCompoundBuilder.() -> Unit) {
        add(nbtCompound(build))
    }

    inline fun list(build: NbtListBuilder.() -> Unit) {
        add(nbtList(build))
    }

    /**
     * Adds an NBT list (*not* a primitive array) with all elements in `value`.
     *
     * @throws IllegalArgumentException if `T` not one of `Boolean`,
     * `Byte`, `Short`, `Int`, `Long`, `Float`, `Double`, `Char` or `String`.
     */
    inline fun <reified T> list(value: Iterable<T>) {
        when (T::class) {
            Boolean::class -> list { value.forEach { add(it as Boolean) } }
            Byte::class -> list { value.forEach { add(it as Byte) } }
            Short::class -> list { value.forEach { add(it as Short) } }
            Int::class -> list { value.forEach { add(it as Int) } }
            Long::class -> list { value.forEach { add(it as Long) } }
            Float::class -> list { value.forEach { add(it as Float) } }
            Double::class -> list { value.forEach { add(it as Double) } }
            Char::class -> list { value.forEach { add(it as Char) } }
            String::class -> list { value.forEach { add(it as String) } }
            else -> throw IllegalArgumentException("Type ${T::class} is not a valid NBT type")
        }
    }

    fun byteArray(vararg value: Byte) {
        add(value.toNbt())
    }

    fun byteArray(value: Collection<Byte>) = byteArray(*value.toByteArray())

    fun intArray(vararg value: Int) {
        add(value.toNbt())
    }

    fun intArray(value: Collection<Int>) = intArray(*value.toIntArray())

    fun longArray(vararg value: Long) {
        add(value.toNbt())
    }

    fun longArray(value: Collection<Long>) = longArray(*value.toLongArray())

    fun build() = list
}
