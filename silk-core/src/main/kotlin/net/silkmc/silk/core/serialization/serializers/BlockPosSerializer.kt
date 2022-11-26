package net.silkmc.silk.core.serialization.serializers

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.minecraft.core.Vec3i
import net.minecraft.core.BlockPos
import net.silkmc.silk.core.serialization.SilkSerializer

/**
 * Use vector serializers for structure. [BlockPos] is a [Vec3i]
 */
@ExperimentalSerializationApi
object BlockPosSerializer : SilkSerializer<BlockPos>() {
    override val descriptor = PrimitiveSerialDescriptor(descriptorName, PrimitiveKind.LONG)

    override fun deserialize(decoder: Decoder) = BlockPos.of(decoder.decodeLong())!!

    override fun serialize(encoder: Encoder, value: BlockPos) = encoder.encodeLong(value.asLong())
}