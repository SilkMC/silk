package net.axay.fabrik.nbt

import kotlinx.serialization.*
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import net.axay.fabrik.nbt.decoder.NbtRootDecoder
import net.axay.fabrik.nbt.encoder.NbtRootEncoder
import net.minecraft.nbt.NbtElement

@OptIn(ExperimentalSerializationApi::class)
sealed class Nbt(val config: NbtConfig, val serializersModule: SerializersModule) {
    companion object Default : Nbt(NbtConfig(), EmptySerializersModule)

    fun <T> encodeToNbtElement(serializer: SerializationStrategy<T>, value: T): NbtElement =
        NbtRootEncoder(this).apply { encodeSerializableValue(serializer, value) }.element
            ?: throw SerializationException("Serializer did not encode any element")

    fun <T> decodeFromNbtElement(deserializer: DeserializationStrategy<T>, element: NbtElement): T =
        NbtRootDecoder(this, element).decodeSerializableValue(deserializer)
}

private class NbtImpl(config: NbtConfig, serializersModule: SerializersModule) : Nbt(config, serializersModule)

data class NbtConfig(
    val encodeDefaults: Boolean = false,
    val ignoreUnknownKeys: Boolean = false
)

inline fun Nbt(from: Nbt = Nbt.Default, build: NbtBuilder.() -> Unit): Nbt =
    NbtBuilder(from).apply(build).build()

class NbtBuilder(from: Nbt) {
    var encodeDefaults = from.config.encodeDefaults
    var ignoreUnknownKeys = from.config.ignoreUnknownKeys

    var serializersModule = from.serializersModule

    fun build(): Nbt = NbtImpl(NbtConfig(), serializersModule)
}

inline fun <reified T> Nbt.encodeToNbtElement(value: T) =
    encodeToNbtElement(serializer(), value)

inline fun <reified T> Nbt.decodeFromNbtElement(element: NbtElement) =
    decodeFromNbtElement(serializer<T>(), element)
