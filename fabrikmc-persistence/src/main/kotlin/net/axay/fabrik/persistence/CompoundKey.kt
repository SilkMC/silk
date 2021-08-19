package net.axay.fabrik.persistence

import net.axay.fabrik.core.logging.logWarning
import net.axay.fabrik.nbt.Nbt
import net.axay.fabrik.nbt.decodeFromNbtElement
import net.axay.fabrik.nbt.encodeToNbtElement
import net.minecraft.nbt.NbtElement
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import kotlin.reflect.full.isSubclassOf

/**
 * Creates a [CompoundKey] which can be used to read and write data
 * to a [PersistentCompound] in a typesafe way.
 *
 * @param name an optional name for this compound value - defaults to the name of
 * the property with the suffix "Key" being removed
 */
inline fun <reified T> compoundKey(name: String? = null) =
    CompoundKeyProperty(
        object : CompoundKey<T>() {
            init {
                if (T::class.isSubclassOf(NbtElement::class))
                    logWarning("Usage of compoundKey function with NbtElement as type detected! You probably want to use nbtElementCompoundKey instead.")
            }

            override var name = name

            override fun serializeValueToNbtElement(compound: PersistentCompound) =
                Nbt.encodeToNbtElement(values[compound]!!)

            override fun deserializeValueFromNbtElement(nbtElement: NbtElement) =
                Nbt.decodeFromNbtElement<T>(nbtElement)
        }
    )

/**
 * Creates a [CompoundKey] which can be used to read and write data
 * to a [PersistentCompound] in a typesafe way.
 *
 * This compound key is meant specifically for values of the type [NbtElement].
 * These can be treated differently, as they don't have to be converted
 * or serialized.
 *
 * @param name an optional name for this compound value - defaults to the name of
 * the property with the suffix "Key" being removed
 */
inline fun <reified T : NbtElement> nbtElementCompoundKey(name: String? = null) =
    CompoundKeyProperty(
        object : NbtElementCompoundKey<T>() {
            override var name = name
        }
    )

abstract class CompoundKey<T> {
    abstract var name: String?
        internal set

    @PublishedApi
    internal val values = HashMap<PersistentCompound, T>()

    abstract fun serializeValueToNbtElement(compound: PersistentCompound): NbtElement

    abstract fun deserializeValueFromNbtElement(nbtElement: NbtElement): T
}

abstract class NbtElementCompoundKey<T : NbtElement> : CompoundKey<T>() {
    override fun serializeValueToNbtElement(compound: PersistentCompound) =
        values[compound]!!

    @Suppress("UNCHECKED_CAST")
    override fun deserializeValueFromNbtElement(nbtElement: NbtElement) =
        nbtElement as T
}

class CompoundKeyProperty<T>(private val compoundKey: CompoundKey<T>) : ReadOnlyProperty<Any?, CompoundKey<T>> {
    override fun getValue(thisRef: Any?, property: KProperty<*>): CompoundKey<T> {
        if (compoundKey.name == null)
            compoundKey.name = property.name.removeSuffix("Key")

        return compoundKey
    }
}
