package net.silkmc.silk.core.serialization.serializers

import com.mojang.math.Vector3d
import com.mojang.math.Vector3f
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.minecraft.core.Vec3i
import net.silkmc.silk.core.math.vector.toTriple
import net.silkmc.silk.core.serialization.SilkSerializer

private const val xIndex = 0
private const val yIndex = 1
private const val zIndex = 2

private inline fun <reified T : Number> SilkSerializer<*>.descriptor(serializer: KSerializer<T>) =
    buildClassSerialDescriptor(descriptorName) {
        element("x", serializer.descriptor)
        element("y", serializer.descriptor)
        element("z", serializer.descriptor)
    }

@ExperimentalSerializationApi
private inline fun <reified T : Number, U> CompositeDecoder.decodeVector(
    descriptor: SerialDescriptor,
    serializer: DeserializationStrategy<T>,
    constructor: (T, T, T) -> U
) = run {
    if (decodeSequentially()) {
        constructor(
            decodeSerializableElement(descriptor, xIndex, serializer),
            decodeSerializableElement(descriptor, yIndex, serializer),
            decodeSerializableElement(descriptor, zIndex, serializer)
        )
    } else {
        var x: T? = null
        var y: T? = null
        var z: T? = null
        while (true) {
            when (val i = decodeElementIndex(Vec3iSerializer.descriptor)) {
                CompositeDecoder.DECODE_DONE -> break
                xIndex -> x = decodeSerializableElement(descriptor, i, serializer)
                yIndex -> y = decodeSerializableElement(descriptor, i, serializer)
                zIndex -> z = decodeSerializableElement(descriptor, i, serializer)
            }
        }
        constructor(x!!, y!!, z!!)
    }
}

private fun <T : Number> CompositeEncoder.encodeVector(
    descriptor: SerialDescriptor,
    serializer: SerializationStrategy<T>,
    triple: Triple<T, T, T>
) = apply {
    encodeSerializableElement(descriptor, xIndex, serializer, triple.first)
    encodeSerializableElement(descriptor, yIndex, serializer, triple.second)
    encodeSerializableElement(descriptor, zIndex, serializer, triple.third)
}

@ExperimentalSerializationApi
object Vec3iSerializer : SilkSerializer<Vec3i>() {
    private val serializer = Int.serializer()

    override val descriptor = descriptor(serializer)

    override fun deserialize(decoder: Decoder) =
        decoder.beginStructure(descriptor).decodeVector(descriptor, serializer, (::Vec3i))

    override fun serialize(encoder: Encoder, value: Vec3i) =
        encoder.beginStructure(descriptor).encodeVector(descriptor, serializer, value.toTriple()).endStructure(descriptor)
}

@ExperimentalSerializationApi
object Vector3fSerializer : SilkSerializer<Vector3f>() {
    private val serializer = Float.serializer()

    override val descriptor = descriptor(serializer)

    override fun deserialize(decoder: Decoder) =
        decoder.beginStructure(descriptor).decodeVector(descriptor, serializer, (::Vector3f))

    override fun serialize(encoder: Encoder, value: Vector3f) =
        encoder.beginStructure(descriptor).encodeVector(descriptor, serializer, value.toTriple()).endStructure(descriptor)
}

@ExperimentalSerializationApi
object Vector3dSerializer : SilkSerializer<Vector3d>() {
    private val serializer = Double.serializer()

    override val descriptor = descriptor(serializer)

    override fun deserialize(decoder: Decoder) =
        decoder.beginStructure(descriptor).decodeVector(descriptor, serializer, (::Vector3d))

    override fun serialize(encoder: Encoder, value: Vector3d) =
        encoder.beginStructure(descriptor).encodeVector(descriptor, serializer, value.toTriple()).endStructure(descriptor)
}