package net.axay.fabrik.persistence

import net.axay.fabrik.core.logging.logError
import net.axay.fabrik.nbt.Nbt
import net.axay.fabrik.nbt.decodeFromNbtElement
import net.axay.fabrik.nbt.encodeToNbtElement
import net.minecraft.nbt.NbtElement
import net.minecraft.util.Identifier
import kotlin.reflect.full.isSubclassOf

/**
 * Creates a [CompoundKey] which can be used to read and write data
 * to a [PersistentCompound] in a typesafe way.
 *
 * @param name an optional name for this compound value - defaults to the name of
 * the property with the suffix "Key" being removed
 */
inline fun <reified T> compoundKey(id: Identifier) =
    object : CompoundKey<T>(id) {
        init {
            if (T::class.isSubclassOf(NbtElement::class))
                logError("Usage of compoundKey function with NbtElement as type detected! You probably want to use nbtElementCompoundKey instead.")
        }

        override fun convertValueToNbtElement(value: T) =
            Nbt.encodeToNbtElement(value)

        override fun convertNbtElementToValue(nbtElement: NbtElement) =
            Nbt.decodeFromNbtElement<T>(nbtElement)
    }

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
inline fun <reified T : NbtElement> nbtElementCompoundKey(id: Identifier) =
    object : CompoundKey<T>(id) {
        override fun convertValueToNbtElement(value: T) = value

        override fun convertNbtElementToValue(nbtElement: NbtElement) = nbtElement as T
    }

abstract class CompoundKey<T>(val name: String) {
    constructor(id: Identifier) : this(id.toString())

    private val values = HashMap<PersistentCompound, T>()

    internal fun setValue(compound: PersistentCompound, value: T) {
        values[compound] = value
    }

    internal fun removeValue(compound: PersistentCompound) {
        values -= compound
    }

    internal fun getNbtElement(compound: PersistentCompound): NbtElement? {
        return values[compound]?.let { convertValueToNbtElement(it) }
    }

    internal inline fun getValue(compound: PersistentCompound, ifConverted: () -> Unit = {}): T? {
        return values[compound] ?: compound.data?.get(name)
            ?.let { convertNbtElementToValue(it) }
            ?.also {
                setValue(compound, it)
                ifConverted()
            }
    }

    protected abstract fun convertValueToNbtElement(value: T): NbtElement

    protected abstract fun convertNbtElementToValue(nbtElement: NbtElement): T
}
