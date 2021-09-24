package net.axay.fabrik.nbt.serialization

import kotlinx.serialization.*
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import net.axay.fabrik.nbt.serialization.decoder.NbtRootDecoder
import net.axay.fabrik.nbt.serialization.encoder.NbtRootEncoder
import net.minecraft.nbt.NbtElement

/**
 * Instances of this class can encode values to [NbtElement]s and decode
 * [NbtElement]s to values.
 */
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

/**
 * Creates a new instace of [Nbt]. This function allows you to customize the
 * behaviour of NBT serialization and deserialization.
 */
inline fun Nbt(from: Nbt = Nbt.Default, build: NbtBuilder.() -> Unit): Nbt =
    NbtBuilder(from).apply(build).build()

class NbtBuilder(from: Nbt) {
    var encodeDefaults = from.config.encodeDefaults
    var ignoreUnknownKeys = from.config.ignoreUnknownKeys

    var serializersModule = from.serializersModule

    fun build(): Nbt = NbtImpl(NbtConfig(encodeDefaults, ignoreUnknownKeys), serializersModule)
}

/**
 * Encodes the given [value] to an [NbtElement]. If the given value of the type [T]
 * can be represented by a primitive NbtElement, such an element will be the result of this
 * function. Otherwise, an [net.minecraft.nbt.NbtCompound] will be created.
 */
inline fun <reified T> Nbt.encodeToNbtElement(value: T) =
    encodeToNbtElement(serializer(), value)

/**
 * Encodes the given [element] to an instance of the class [T].
 *
 * If the [element] does not contain all necessary entries, an exception
 * will be thrown.
 */
inline fun <reified T> Nbt.decodeFromNbtElement(element: NbtElement) =
    decodeFromNbtElement(serializer<T>(), element)

/**
 * Thrown if [NbtConfig.ignoreUnknownKeys] is set to false and an unknown key
 * is present during deserialization.
 */
class UnknownKeyException(val key: String) : SerializationException("Encountered unknown key '$key'")
