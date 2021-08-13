package net.axay.fabrik.nbt

import kotlinx.serialization.*
import net.axay.fabrik.nbt.encoder.NbtRootEncoder
import net.minecraft.nbt.NbtElement

@OptIn(ExperimentalSerializationApi::class)
sealed class Nbt {
    companion object Default : Nbt()

    fun <T> encodeToNbtElement(serializer: SerializationStrategy<T>, value: T): NbtElement =
        NbtRootEncoder().apply { encodeSerializableValue(serializer, value) }.element
            ?: throw SerializationException("Serializer did not encode any element")

    fun <T> decodeFromNbtElement(deserializer: DeserializationStrategy<T>, element: NbtElement) {

    }
}

inline fun <reified T> Nbt.encodeToNbtElement(value: T) =
    encodeToNbtElement(serializer(), value)

inline fun <reified T> Nbt.decodeFromNbtElement(element: NbtElement) =
    decodeFromNbtElement(serializer<T>(), element)
