package net.axay.fabrik.nbt

import kotlinx.serialization.*
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import net.axay.fabrik.nbt.encoder.NbtRootEncoder
import net.minecraft.nbt.NbtElement

@OptIn(ExperimentalSerializationApi::class)
sealed class Nbt(val serializersModule: SerializersModule) {
    companion object Default : Nbt(EmptySerializersModule)

    fun <T> encodeToNbtElement(serializer: SerializationStrategy<T>, value: T): NbtElement =
        NbtRootEncoder(serializersModule).apply { encodeSerializableValue(serializer, value) }.element
            ?: throw SerializationException("Serializer did not encode any element")

    fun <T> decodeFromNbtElement(deserializer: DeserializationStrategy<T>, element: NbtElement) {

    }
}

inline fun <reified T> Nbt.encodeToNbtElement(value: T) =
    encodeToNbtElement(serializer(), value)

inline fun <reified T> Nbt.decodeFromNbtElement(element: NbtElement) =
    decodeFromNbtElement(serializer<T>(), element)
