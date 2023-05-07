package net.silkmc.silk.nbt.serialization.serializer

import kotlinx.serialization.*
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.minecraft.nbt.*
import net.silkmc.silk.core.serialization.SilkPrimitiveSerializer
import net.silkmc.silk.core.serialization.SilkSerializer
import net.silkmc.silk.nbt.serialization.decoder.TagDecoder
import kotlin.reflect.KClass

@ExperimentalSerializationApi
@OptIn(InternalSerializationApi::class)
object BaseTagSerializer : SilkSerializer<Tag>(Tag::class) {
    private val serializer = PolymorphicSerializer(Tag::class)
    override val descriptor: SerialDescriptor =
        buildSerialDescriptor(descriptorName, PolymorphicKind.SEALED)

    override fun deserialize(decoder: Decoder) = run {
        if (decoder is TagDecoder) decoder.nextMaybeNullable()
        else serializer.deserialize(decoder)
    }

    override fun serialize(encoder: Encoder, value: Tag) {
        serializer.serialize(encoder, value)
    }
}

@ExperimentalSerializationApi
object CompoundTagSerializer : SilkSerializer<CompoundTag>(CompoundTag::class) {
    private val serializer = MapSerializer(String.serializer(), BaseTagSerializer)
    override val descriptor = SerialDescriptor(descriptorName, serializer.descriptor)
    override fun deserialize(decoder: Decoder) = CompoundTag().apply { serializer.deserialize(decoder).forEach(::put) }
    override fun serialize(encoder: Encoder, value: CompoundTag) =
        serializer.serialize(encoder, value.allKeys.associateWith { value[it]!! })
}

@ExperimentalSerializationApi
object EndTagSerializer : SilkPrimitiveSerializer<EndTag>(PrimitiveKind.BYTE, EndTag::class) {
    override fun deserialize(decoder: Decoder) = EndTag.INSTANCE.also { decoder.decodeByte() }!!
    override fun serialize(encoder: Encoder, value: EndTag) = encoder.encodeByte(0)
}

@ExperimentalSerializationApi
object StringTagSerializer : SilkPrimitiveSerializer<StringTag>(PrimitiveKind.STRING, StringTag::class) {
    override fun deserialize(decoder: Decoder) = StringTag.valueOf(decoder.decodeString())!!
    override fun serialize(encoder: Encoder, value: StringTag) = encoder.encodeString(value.asString)
}

/**
 * NumericTag
 */
@ExperimentalSerializationApi
object ByteTagSerializer : SilkPrimitiveSerializer<ByteTag>(PrimitiveKind.BYTE, ByteTag::class) {
    override fun deserialize(decoder: Decoder) = ByteTag.valueOf(decoder.decodeByte())!!
    override fun serialize(encoder: Encoder, value: ByteTag) = encoder.encodeByte(value.asByte)
}

@ExperimentalSerializationApi
object DoubleTagSerializer : SilkPrimitiveSerializer<DoubleTag>(PrimitiveKind.DOUBLE, DoubleTag::class) {
    override fun deserialize(decoder: Decoder) = DoubleTag.valueOf(decoder.decodeDouble())!!
    override fun serialize(encoder: Encoder, value: DoubleTag) = encoder.encodeDouble(value.asDouble)
}

@ExperimentalSerializationApi
object FloatTagSerializer : SilkPrimitiveSerializer<FloatTag>(PrimitiveKind.FLOAT, FloatTag::class) {
    override fun deserialize(decoder: Decoder) = FloatTag.valueOf(decoder.decodeFloat())!!
    override fun serialize(encoder: Encoder, value: FloatTag) = encoder.encodeFloat(value.asFloat)
}

@ExperimentalSerializationApi
object IntTagSerializer : SilkPrimitiveSerializer<IntTag>(PrimitiveKind.INT, IntTag::class) {
    override fun deserialize(decoder: Decoder) = IntTag.valueOf(decoder.decodeInt())!!
    override fun serialize(encoder: Encoder, value: IntTag) = encoder.encodeInt(value.asInt)
}

@ExperimentalSerializationApi
object LongTagSerializer : SilkPrimitiveSerializer<LongTag>(PrimitiveKind.LONG, LongTag::class) {
    override fun deserialize(decoder: Decoder) = LongTag.valueOf(decoder.decodeLong())!!
    override fun serialize(encoder: Encoder, value: LongTag) = encoder.encodeLong(value.asLong)
}

@ExperimentalSerializationApi
object ShortTagSerializer : SilkPrimitiveSerializer<ShortTag>(PrimitiveKind.SHORT, ShortTag::class) {
    override fun deserialize(decoder: Decoder) = ShortTag.valueOf(decoder.decodeShort())!!
    override fun serialize(encoder: Encoder, value: ShortTag) = encoder.encodeShort(value.asShort)
}

/**
 * CollectionTag
 */
@ExperimentalSerializationApi
sealed class CollectionTagSerializer<U : Tag, T : CollectionTag<U>>(
    val tag: SilkSerializer<U>,
    baseClass: KClass<T>,
    private val constructor: (List<U>) -> T
) : SilkSerializer<T>(baseClass) {
    private val serializer = ListSerializer(tag)
    override val descriptor = SerialDescriptor(descriptorName, serializer.descriptor)
    override fun deserialize(decoder: Decoder) = constructor(serializer.deserialize(decoder))
    override fun serialize(encoder: Encoder, value: T) = serializer.serialize(encoder, value)
}

@ExperimentalSerializationApi
object ListTagSerializer :
    CollectionTagSerializer<Tag, ListTag>(BaseTagSerializer, ListTag::class, {
        ListTag().apply {
            addAll(it)
        }
    })

@ExperimentalSerializationApi
object ByteArrayTagSerializer :
    CollectionTagSerializer<ByteTag, ByteArrayTag>(ByteTagSerializer, ByteArrayTag::class, { list -> ByteArrayTag(list.map { it.asByte }) })

@ExperimentalSerializationApi
object IntArrayTagSerializer :
    CollectionTagSerializer<IntTag, IntArrayTag>(IntTagSerializer, IntArrayTag::class, { list -> IntArrayTag(list.map { it.asInt }) })

@ExperimentalSerializationApi
object LongArrayTagSerializer :
    CollectionTagSerializer<LongTag, LongArrayTag>(LongTagSerializer, LongArrayTag::class, { list -> LongArrayTag(list.map { it.asLong }) })