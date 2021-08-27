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
     */
    fun remove(key: CompoundKey<*>) {
        if (data == null) return

        data!!.remove(key.name)
        values -= key
    }

    /**
     * Removes the current value associated with the given [key].
     *
     * @see remove
     */
    operator fun minusAssign(key: CompoundKey<*>) =
        remove(key)

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
class PersistentCompoundImpl : PersistentCompound() {
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
