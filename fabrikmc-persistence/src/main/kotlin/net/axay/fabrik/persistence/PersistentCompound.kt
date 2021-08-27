package net.axay.fabrik.persistence

import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement

/**
 * Holds data which can be accessed fast because it is stored in memory.
 *
 * Additionally, the data will be stored persistently to the disk, if the
 * game decides to do so.
 */
abstract class PersistentCompound {
    @PublishedApi
    internal abstract var data: NbtCompound?

    @PublishedApi
    internal val values = HashMap<CompoundKey<*>, Any>()

    /**
     * Puts the given value into the persistent storage.
     *
     * Values **have to** be serializable. Annotate them with
     * [kotlinx.serialization.Serializable] to enable support for fast
     * serialization.
     *
     * An exception to the above are values of the type [NbtElement].
     * You can use these to skip serialization and deserialization. It is not
     * as convenient to work with them, but they are faster.
     */
    operator fun <T : Any> set(key: CompoundKey<T>, value: T) {
        if (data == null) return

        values[key] = value
    }

    /**
     * Tries to get the value for the given [key] and convert it
     * to the given type [T].
     *
     * The access is fast, as it caches deserialized values. But keep in mind
     * that if a value has been deserialized by the game itself it has to
     * be deserialized by FabrikMC once (lazily) before it can be cached into memory.
     *
     * Note: this function will only return null if the value is not present, it
     * will still throw an exception if cast or conversion to the given type [T] failed
     */
    inline operator fun <reified T : Any> get(key: CompoundKey<T>): T? {
        if (data == null) return null

        return (values[key] as T?)
            ?: data!!.get(key.name)
                ?.let { key.convertNbtElementToValue(it) }
                ?.also { values[key] = it }
    }

    /**
     * Removes the current value associated with the given [key].
     *
     * This function does not return the removed value, if you need that
     * use [getAndRemove].
     */
    fun remove(key: CompoundKey<*>) {
        if (data == null) return

        data!!.remove(key.name)
        values -= key
    }

    /**
     * Removes the current value associated with the given [key]. Additionally,
     * this function returns the value that was removed, **if** one was removed.
     * Otherwise, the return value will be null.
     *
     * Note: Calling this function may result in deserialization of the value which
     * was deleted from the internal [NbtCompound], if the removed value was not
     * loaded into memory.
     */
    inline fun <reified T : Any> getAndRemove(key: CompoundKey<T>): T? {
        if (data == null) return null

        return ((values.remove(key) as T?) ?: data!!.get(key.name)?.let { key.convertNbtElementToValue(it) })
            .also { data!!.remove(key.name) }
    }

    /**
     * Clears all persistent data from this compound.
     *
     * **Be aware that this deletes data of other mods as well!**
     */
    fun clear() {
        if (data == null) return

        values.clear()
        data = NbtCompound()
    }

    /**
     * Removes the current value associated with the given [key].
     *
     * As this function is an operator function, it does not return the
     * removed value.
     *
     * @see remove
     */
    inline operator fun <reified T : Any> minusAssign(key: CompoundKey<T>) {
        remove(key)
    }

    /**
     * Executes the [get] function. If the result of the [get] function is null
     * the [defaultValue] will be evaluated and put into the compound.
     *
     * This function won't return null, it either returns the value in already present
     * in the compound, or it will return the evaluated default value, after having
     * stored it as well.
     *
     * @see get
     */
    inline fun <reified T : Any> getOrPut(key: CompoundKey<T>, defaultValue: () -> T): T {
        if (data == null) return defaultValue()

        return this[key] ?: defaultValue().also { this[key] = it }
    }

    @PublishedApi
    internal abstract fun loadFromCompound(nbtCompound: NbtCompound)

    @PublishedApi
    internal abstract fun putInCompound(nbtCompound: NbtCompound)
}

/**
 * A [PersistentCompound] which does nothing.
 * Needed for empty holders such as [net.minecraft.world.chunk.EmptyChunk] for example.
 */
object EmptyPersistentCompound : PersistentCompound() {
    override var data: NbtCompound? = null

    override fun loadFromCompound(nbtCompound: NbtCompound) = Unit
    override fun putInCompound(nbtCompound: NbtCompound) = Unit
}

/**
 * The [PersistentCompound] implementation used by all normal
 * [CompoundProvider]s.
 */
internal class PersistentCompoundImpl : PersistentCompound() {
    companion object {
        private const val CUSTOM_DATA_KEY = "fabrikmcData"
    }

    override var data: NbtCompound? = NbtCompound()

    override fun loadFromCompound(nbtCompound: NbtCompound) {
        data = nbtCompound.getCompound(CUSTOM_DATA_KEY)
    }

    override fun putInCompound(nbtCompound: NbtCompound) {
        for ((untypedKey, value) in values) {
            @Suppress("UNCHECKED_CAST")
            val typedKey = untypedKey as CompoundKey<Any>

            data!!.put(typedKey.name, typedKey.convertValueToNbtElement(value))
        }

        if (!data!!.isEmpty) {
            nbtCompound.put(CUSTOM_DATA_KEY, data)
        }
    }
}
