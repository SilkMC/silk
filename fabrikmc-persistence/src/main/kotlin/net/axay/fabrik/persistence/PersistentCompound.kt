package net.axay.fabrik.persistence

import net.axay.fabrik.nbt.Nbt
import net.axay.fabrik.nbt.decodeFromNbtElement
import net.axay.fabrik.nbt.encodeToNbtElement
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import kotlin.reflect.full.isSubclassOf

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
    internal abstract val valuesMap: MutableMap<String, Any>?

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
    abstract operator fun set(key: String, value: Any)

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
    inline fun <reified T : Any> getOrNull(key: String): T? {
        if (valuesMap == null) return null

        val value = valuesMap!![key]?.let { it as? T }

        // try to load / reload the value if it was not cached
        // or if the cast failed
        return if (value == null) {
            val newValue = if (T::class.isSubclassOf(NbtElement::class))
                data?.get(key) as T?
            else
                data?.get(key)?.let { Nbt.decodeFromNbtElement(it) }

            if (newValue != null) {
                valuesMap!![key] = newValue
            }

            newValue
        } else value
    }

    /**
     * Does the same as [getOrNull] but with the assumption that the value
     * is present.
     *
     * This will throw a [NullPointerException] if the value is not present.
     */
    inline operator fun <reified T : Any> get(key: String): T =
        getOrNull(key)!!

    internal abstract fun loadFromCompound(nbtCompound: NbtCompound)
    internal abstract fun putInCompound(nbtCompound: NbtCompound)
}

/**
 * A [PersistentCompound] which does nothing.
 * Needed for empty holders such as [net.minecraft.world.chunk.EmptyChunk] for example.
 */
object EmptyPersistentCompound : PersistentCompound() {
    override val data: Nothing? = null
    override val valuesMap: Nothing? = null

    override fun set(key: String, value: Any) = Unit

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

    override val valuesMap = HashMap<String, Any>()

    override fun set(key: String, value: Any) {
        valuesMap[key] = value
    }

    override fun loadFromCompound(nbtCompound: NbtCompound) {
        data = nbtCompound.getCompound(CUSTOM_DATA_KEY)
    }

    override fun putInCompound(nbtCompound: NbtCompound) {
        for ((key, value) in valuesMap) {
            nbtCompound.put(key, if (value is NbtElement) value else Nbt.encodeToNbtElement(value))
        }
        if (!data.isEmpty)
            nbtCompound.put(CUSTOM_DATA_KEY, data)
    }
}
