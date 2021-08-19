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
    internal abstract val data: NbtCompound?

    @PublishedApi
    internal abstract val compoundKeys: MutableSet<CompoundKey<*>>?

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
    inline operator fun <reified T : Any> set(key: CompoundKey<T>, value: T) {
        if (compoundKeys != null) {
            key.values[this] = value
            compoundKeys!! += key
        }
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
        if (compoundKeys == null) return null

        val value = key.values[this]

        // try to load / reload the value if it was not cached
        return if (value == null) {
            val nbtElement = data?.get(key.name)

            val newValue = if (nbtElement != null)
                key.deserializeValueFromNbtElement(nbtElement)
            else
                null

            if (newValue != null) {
                this[key] = newValue
            }

            newValue
        } else value
    }

    internal abstract fun loadFromCompound(nbtCompound: NbtCompound)
    internal abstract fun putInCompound(nbtCompound: NbtCompound)
}

/**
 * A [PersistentCompound] which does nothing.
 * Needed for empty holders such as [net.minecraft.world.chunk.EmptyChunk] for example.
 */
object EmptyPersistentCompound : PersistentCompound() {
    override val data: Nothing? = null
    override val compoundKeys: Nothing? = null

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

    override var data = NbtCompound()

    override val compoundKeys = HashSet<CompoundKey<*>>()

    override fun loadFromCompound(nbtCompound: NbtCompound) {
        data = nbtCompound.getCompound(CUSTOM_DATA_KEY)
    }

    override fun putInCompound(nbtCompound: NbtCompound) {
        for (key in compoundKeys) {
            data.put(key.name, key.serializeValueToNbtElement(this))

            // ensure that both the key and this persistent compound
            // are garbage collectible, as this function could be the
            // last one called on this instance
            key.values -= this
        }
        compoundKeys.clear()

        if (!data.isEmpty) {
            nbtCompound.put(CUSTOM_DATA_KEY, data)
        }
    }
}
