package net.axay.fabrik.persistence

import net.axay.fabrik.nbt.Nbt
import net.axay.fabrik.nbt.encodeToNbtElement
import net.minecraft.nbt.NbtCompound

/**
 * Holds data which can be accessed fast because it is stored in memory.
 *
 * Additionally, the data will be stored persistently to the disk, if the
 * game decides to do so.
 */
abstract class PersistentCompound {
    /**
     * Puts the given value into the persistent storage.
     *
     * Values of a type which can be represented by NBT natively will be
     * converted in a lightweight way.
     *
     * Other values **have to** be serializable. Annotate them with
     * [kotlinx.serialization.Serializable] to enable support for fast
     * serialization.
     */
    abstract operator fun set(key: String, value: Any)

    /**
     * Puts the given collection into the persistent storage.
     *
     * Collections with a generic type of [Byte], [Int] or [Long] will
     * be stored effeciently as a Long internally.
     */
    abstract operator fun set(key: String, value: Collection<Any>)

    internal abstract fun loadFromCompound(nbtCompound: NbtCompound)
    internal abstract fun putInCompound(nbtCompound: NbtCompound)
}

/**
 * A [PersistentCompound] which does nothing.
 * Needed for empty holders such as [net.minecraft.world.chunk.EmptyChunk] for example.
 */
object EmptyPersistentCompound : PersistentCompound() {
    override fun set(key: String, value: Collection<Any>) = Unit
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

    private var data = NbtCompound()

    private val valuesMap = HashMap<String, PersistentCompoundValue>()

    override fun set(key: String, value: Any) {
        valuesMap[key] = NativePersistentCompoundValue.fromValueOrNull(value)
            ?: SerializablePersistentCompoundValue(value)
    }

    override fun set(key: String, value: Collection<Any>) {
        valuesMap[key] = CollectionPersistentCompoundValue(value)
    }

    override fun loadFromCompound(nbtCompound: NbtCompound) {
        data = nbtCompound.getCompound(CUSTOM_DATA_KEY)
    }

    override fun putInCompound(nbtCompound: NbtCompound) {
        for ((key, value) in valuesMap) {
            value.putInCompound(data, key)
        }
        if (!data.isEmpty)
            nbtCompound.put(CUSTOM_DATA_KEY, data)
    }
}

internal interface PersistentCompoundValue {
    fun putInCompound(nbtCompound: NbtCompound, key: String)
}

@PublishedApi
internal class NativePersistentCompoundValue(private val value: Any) : PersistentCompoundValue {
    companion object {
        @PublishedApi
        internal fun fromValueOrNull(value: Any): PersistentCompoundValue? {
            val isNative = when (value) {
                is Boolean -> true
                is Byte, is Short, is Int, is Long -> true
                is Float, is Double -> true
                is String -> true
                is ByteArray, is IntArray, is LongArray -> true
                is Array<*> -> {
                    when (value::class) {
                        Array<Byte>::class, Array<Int>::class, Array<Long>::class -> true
                        else -> false
                    }
                }
                else -> false
            }
            return if (isNative) NativePersistentCompoundValue(value) else null
        }
    }

    override fun putInCompound(nbtCompound: NbtCompound, key: String) {
        nbtCompound.put(
            key,
            value.toPrimitiveNbtOrNull()
                ?: error("Could not convert value of the type ${value::class.qualifiedName} to an NbtElement")
        )
    }
}

@PublishedApi
internal class SerializablePersistentCompoundValue(private val value: Any) : PersistentCompoundValue {
    override fun putInCompound(nbtCompound: NbtCompound, key: String) {
        nbtCompound.put(key, Nbt.encodeToNbtElement(value))
    }
}

@PublishedApi
internal class CollectionPersistentCompoundValue(private val iterable: Collection<Any>) : PersistentCompoundValue {
    override fun putInCompound(nbtCompound: NbtCompound, key: String) {
        nbtCompound.put(key, iterable.toNbt())
    }
}
