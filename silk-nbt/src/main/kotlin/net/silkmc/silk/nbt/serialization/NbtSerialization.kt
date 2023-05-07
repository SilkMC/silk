package net.silkmc.silk.nbt.serialization

import kotlinx.serialization.*
import kotlinx.serialization.modules.*
import net.minecraft.nbt.Tag
import net.silkmc.silk.nbt.serialization.decoder.RootTagDecoder
import net.silkmc.silk.nbt.serialization.encoder.RootTagEncoder
import net.silkmc.silk.nbt.serialization.serializer.*

@ExperimentalSerializationApi
val TagsModule = SerializersModule {
    polymorphic(Tag::class) {
        subclass(CompoundTagSerializer)
        subclass(EndTagSerializer)
        subclass(StringTagSerializer)
        subclass(ByteTagSerializer)
        subclass(DoubleTagSerializer)
        subclass(FloatTagSerializer)
        subclass(IntTagSerializer)
        subclass(LongTagSerializer)
        subclass(ListTagSerializer)
        subclass(ByteArrayTagSerializer)
        subclass(IntArrayTagSerializer)
        subclass(LongArrayTagSerializer)
        defaultDeserializer { BaseTagSerializer }
    }
}

/**
 * Instances of this class can encode values to [Tag]s and decode
 * [Tag]s to values.
 */
@OptIn(ExperimentalSerializationApi::class)
sealed class Nbt(val config: NbtConfig, serializersModule: SerializersModule) {
    companion object Default : Nbt(NbtConfig(), EmptySerializersModule())

    val serializersModule = serializersModule + TagsModule

    fun <T> encodeToNbtElement(serializer: SerializationStrategy<T>, value: T): Tag =
        RootTagEncoder(this).apply { encodeSerializableValue(serializer, value) }.element
            ?: throw SerializationException("Serializer did not encode any element")

    fun <T> decodeFromNbtElement(deserializer: DeserializationStrategy<T>, element: Tag): T =
        RootTagDecoder(this, element).decodeSerializableValue(deserializer)
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
 * Encodes the given [value] to an [Tag]. If the given value of the type [T]
 * can be represented by a primitive NbtElement, such an element will be the result of this
 * function. Otherwise, an [net.minecraft.nbt.CompoundTag] will be created.
 */
inline fun <reified T> Nbt.encodeToNbtElement(value: T) =
    encodeToNbtElement(serializersModule.serializer(), value)

/**
 * Encodes the given [element] to an instance of the class [T].
 *
 * If the [element] does not contain all necessary entries, an exception
 * will be thrown.
 */
inline fun <reified T> Nbt.decodeFromNbtElement(element: Tag) =
    decodeFromNbtElement(serializersModule.serializer<T>(), element)

/**
 * Thrown if [NbtConfig.ignoreUnknownKeys] is set to false and an unknown key
 * is present during deserialization.
 */
class UnknownKeyException(val key: String) : SerializationException("Encountered unknown key '$key'")
