package net.axay.fabrik.nbt

import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtList

inline fun nbtCompound(build: NbtCompoundBuilder.() -> Unit) =
    NbtCompoundBuilder().apply(build).build()

inline fun nbtList(build: NbtListBuilder.() -> Unit) =
    NbtListBuilder().apply(build).build()

class NbtCompoundBuilder {
    val compound = NbtCompound()

    fun put(key: String, value: Boolean) {
        compound.put(key, value.toNbt())
    }

    fun put(key: String, value: Byte) {
        compound.put(key, value.toNbt())
    }

    fun put(key: String, value: Short) {
        compound.put(key, value.toNbt())
    }

    fun put(key: String, value: Int) {
        compound.put(key, value.toNbt())
    }

    fun put(key: String, value: Long) {
        compound.put(key, value.toNbt())
    }

    fun put(key: String, value: Float) {
        compound.put(key, value.toNbt())
    }

    fun put(key: String, value: Double) {
        compound.put(key, value.toNbt())
    }

    fun put(key: String, value: Char) {
        compound.put(key, value.toNbt())
    }

    fun put(key: String, value: String) {
        compound.put(key, value.toNbt())
    }

    inline fun compound(key: String, build: NbtCompoundBuilder.() -> Unit) {
        compound.put(key, nbtCompound(build))
    }

    inline fun list(key: String, build: NbtListBuilder.() -> Unit) {
        compound.put(key, nbtList(build))
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
        compound.put(key, value.toNbt())
    }

    fun byteArray(key: String, value: Collection<Byte>) =
        byteArray(key, value.toByteArray())

    fun intArray(key: String, value: IntArray) {
        compound.put(key, value.toNbt())
    }

    fun intArray(key: String, value: Collection<Int>) =
        intArray(key, value.toIntArray())

    fun longArray(key: String, value: LongArray) {
        compound.put(key, value.toNbt())
    }

    fun longArray(key: String, value: Collection<Long>) =
        longArray(key, value.toLongArray())

    fun build() = compound
}

class NbtListBuilder {
    val list = NbtList()

    fun add(value: Boolean) {
        list.add(value.toNbt())
    }

    fun add(value: Byte) {
        list.add(value.toNbt())
    }

    fun add(value: Short) {
        list.add(value.toNbt())
    }

    fun add(value: Int) {
        list.add(value.toNbt())
    }

    fun add(value: Long) {
        list.add(value.toNbt())
    }

    fun add(value: Float) {
        list.add(value.toNbt())
    }

    fun add(value: Double) {
        list.add(value.toNbt())
    }

    fun add(value: Char) {
        list.add(value.toNbt())
    }

    fun add(value: String) {
        list.add(value.toNbt())
    }

    inline fun compound(build: NbtCompoundBuilder.() -> Unit) {
        list.add(nbtCompound(build))
    }

    inline fun list(build: NbtListBuilder.() -> Unit) {
        list.add(nbtList(build))
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
        list.add(value.toNbt())
    }

    fun byteArray(value: Collection<Byte>) = byteArray(*value.toByteArray())

    fun intArray(vararg value: Int) {
        list.add(value.toNbt())
    }

    fun intArray(value: Collection<Int>) = intArray(*value.toIntArray())

    fun longArray(vararg value: Long) {
        list.add(value.toNbt())
    }

    fun longArray(value: Collection<Long>) = longArray(*value.toLongArray())

    fun build() = list
}
