package net.axay.fabrik.persistence

import net.axay.fabrik.core.logging.logFatal
import net.axay.fabrik.nbt.serialization.Nbt
import net.axay.fabrik.nbt.serialization.decodeFromNbtElement
import net.axay.fabrik.nbt.serialization.encodeToNbtElement
import net.minecraft.nbt.Tag
import net.minecraft.resources.ResourceLocation
import kotlin.reflect.full.isSubclassOf

/**
 * Creates a [CompoundKey] which can be used to read and write data
 * to a [PersistentCompound] in a typesafe way.
 *
 * @param id the unique identifier for this key, this should contain your mod id
 */
inline fun <reified T : Any> compoundKey(id: ResourceLocation) =
    object : CompoundKey<T>(id) {
        init {
            if (T::class.isSubclassOf(Tag::class))
                logFatal("Usage of compoundKey function with NbtElement as type detected! You probably want to use nbtElementCompoundKey instead.")
        }

        override fun convertValueToNbtElement(value: T) =
            Nbt.encodeToNbtElement(value)

        override fun convertNbtElementToValue(nbtElement: Tag) =
            Nbt.decodeFromNbtElement<T>(nbtElement)
    }

/**
 * Creates a [CompoundKey] which can be used to read and write data
 * to a [PersistentCompound] in a typesafe way.
 *
 * This compound key is meant specifically for values of the type [Tag].
 * These can be treated differently, as they don't have to be converted
 * or serialized.
 *
 * @param id the unique identifier for this key, this should contain your mod id
 */
inline fun <reified T : Tag> nbtElementCompoundKey(id: ResourceLocation) =
    object : CompoundKey<T>(id) {
        override fun convertValueToNbtElement(value: T) = value

        override fun convertNbtElementToValue(nbtElement: Tag) = nbtElement as T
    }

/**
 * Creates a [CompoundKey] which can be used to read and write data
 * to a [PersistentCompound] in a typesafe way.
 *
 * This compound key allows you to specify custom serialization or conversion logic
 * to convert elements of the type [T] to NbtElements of the type [NbtType].
 *
 * @param id the unique identifier for this key, this should contain your mod id
 */
inline fun <reified T : Any, reified NbtType : Tag> customCompoundKey(
    id: ResourceLocation,
    crossinline valueToNbt: (value: T) -> NbtType,
    crossinline nbtToValue: (nbtElement: NbtType) -> T
) = object : CompoundKey<T>(id) {
    override fun convertValueToNbtElement(value: T) = valueToNbt(value)
    override fun convertNbtElementToValue(nbtElement: Tag) = nbtToValue(nbtElement as NbtType)
}

/**
 * Creates a [CompoundKey] which can be used to read and write data
 * to a [PersistentCompound] in a typesafe way.
 *
 * This compound key allows you to specify custom serialization or conversion logic
 * to convert elements of the type [T] to NbtElements of the type [Tag] (this is
 * the version of the [customCompoundKey] function with no specific NbtElement type).
 *
 * @param id the unique identifier for this key, this should contain your mod id
 */
@JvmName("customCompoundKeyNbtElement")
inline fun <reified T : Any> customCompoundKey(
    id: ResourceLocation,
    crossinline convertValueToNbtElement: (value: T) -> Tag,
    crossinline convertNbtElementToValue: (nbtElement: Tag) -> T
) = customCompoundKey<T, Tag>(id, convertValueToNbtElement, convertNbtElementToValue)

abstract class CompoundKey<T : Any>(val name: String) {
    constructor(id: ResourceLocation) : this(id.toString())

    internal abstract fun convertValueToNbtElement(value: T): Tag

    @PublishedApi
    internal abstract fun convertNbtElementToValue(nbtElement: Tag): T
}
