package net.silkmc.silk.nbt.serialization.serializer

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.listSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.minecraft.nbt.*
import net.silkmc.silk.core.serialization.SilkSerializer

// TODO: Unit test
@ExperimentalSerializationApi
abstract class TagSerializer<T : Tag>(
    primitiveKind: PrimitiveKind = PrimitiveKind.STRING
) : SilkSerializer<T>(primitiveKind)

@ExperimentalSerializationApi
object BaseTagSerializer : TagSerializer<Tag>() {
    private val serializer = PolymorphicSerializer(Tag::class)
    override val descriptor = serializer.descriptor
    override fun deserialize(decoder: Decoder) = serializer.deserialize(decoder)
    override fun serialize(encoder: Encoder, value: Tag) = serializer.serialize(encoder, value)
}

@ExperimentalSerializationApi
object CompoundTagSerializer : TagSerializer<CompoundTag>() {
    private val serializer = MapSerializer(String.serializer(), BaseTagSerializer)
    override val descriptor = serializer.descriptor
    override fun deserialize(decoder: Decoder) = CompoundTag().apply { serializer.deserialize(decoder).forEach(::put) }
    override fun serialize(encoder: Encoder, value: CompoundTag) =
        serializer.serialize(encoder, value.allKeys.map { it to value[it]!! }.toMap())
}

@ExperimentalSerializationApi
object EndTagSerializer : TagSerializer<EndTag>(PrimitiveKind.BYTE) {
    override fun deserialize(decoder: Decoder) = EndTag.INSTANCE.also { decoder.decodeByte() }
    override fun serialize(encoder: Encoder, value: EndTag) = encoder.encodeByte(0)
}

@ExperimentalSerializationApi
object StringTagSerializer : TagSerializer<StringTag>(PrimitiveKind.STRING) {
    override fun deserialize(decoder: Decoder) = StringTag.valueOf(decoder.decodeString())!!
    override fun serialize(encoder: Encoder, value: StringTag) = encoder.encodeString(value.asString)
}

/**
 * NumericTag
 */
@ExperimentalSerializationApi
object ByteTagSerializer : TagSerializer<ByteTag>(PrimitiveKind.BYTE) {
    override fun deserialize(decoder: Decoder) = ByteTag.valueOf(decoder.decodeByte())!!
    override fun serialize(encoder: Encoder, value: ByteTag) = encoder.encodeByte(value.asByte)
}

@ExperimentalSerializationApi
object DoubleTagSerializer : TagSerializer<DoubleTag>(PrimitiveKind.DOUBLE) {
    override fun deserialize(decoder: Decoder) = DoubleTag.valueOf(decoder.decodeDouble())!!
    override fun serialize(encoder: Encoder, value: DoubleTag) = encoder.encodeDouble(value.asDouble)
}

@ExperimentalSerializationApi
object FloatTagSerializer : TagSerializer<FloatTag>(PrimitiveKind.FLOAT) {
    override fun deserialize(decoder: Decoder) = FloatTag.valueOf(decoder.decodeFloat())!!
    override fun serialize(encoder: Encoder, value: FloatTag) = encoder.encodeFloat(value.asFloat)
}

@ExperimentalSerializationApi
object IntTagSerializer : TagSerializer<IntTag>(PrimitiveKind.INT) {
    override fun deserialize(decoder: Decoder) = IntTag.valueOf(decoder.decodeInt())!!
    override fun serialize(encoder: Encoder, value: IntTag) = encoder.encodeInt(value.asInt)
}

@ExperimentalSerializationApi
object LongTagSerializer : TagSerializer<LongTag>(PrimitiveKind.LONG) {
    override fun deserialize(decoder: Decoder) = LongTag.valueOf(decoder.decodeLong())!!
    override fun serialize(encoder: Encoder, value: LongTag) = encoder.encodeLong(value.asLong)
}

@ExperimentalSerializationApi
object ShortTagSerializer : TagSerializer<ShortTag>(PrimitiveKind.SHORT) {
    override fun deserialize(decoder: Decoder) = ShortTag.valueOf(decoder.decodeShort())!!
    override fun serialize(encoder: Encoder, value: ShortTag) = encoder.encodeShort(value.asShort)
}

/**
 * CollectionTag
 */
@ExperimentalSerializationApi
open class CollectionTagSerializer<U : Tag, T : CollectionTag<U>>(
    private val tag: TagSerializer<U>,
    private val constructor: (List<U>) -> T
) : TagSerializer<T>() {
    override val descriptor = listSerialDescriptor(tag.descriptor)
    override fun deserialize(decoder: Decoder) = constructor(ListSerializer(tag).deserialize(decoder))
    override fun serialize(encoder: Encoder, value: T) = ListSerializer(tag).serialize(encoder, value)
}

@ExperimentalSerializationApi
object ListTagSerializer :
    CollectionTagSerializer<Tag, ListTag>(BaseTagSerializer, {
        ListTag().apply {
            addAll(it)
        }
    })

@ExperimentalSerializationApi
object ByteArrayTagSerializer :
    CollectionTagSerializer<ByteTag, ByteArrayTag>(ByteTagSerializer, { list -> ByteArrayTag(list.map { it.asByte }) })

@ExperimentalSerializationApi
object IntArrayTagSerializer :
    CollectionTagSerializer<IntTag, IntArrayTag>(IntTagSerializer, { list -> IntArrayTag(list.map { it.asInt }) })

@ExperimentalSerializationApi
object LongArrayTagSerializer :
    CollectionTagSerializer<LongTag, LongArrayTag>(LongTagSerializer, { list -> LongArrayTag(list.map { it.asLong }) })