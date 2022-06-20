package net.silkmc.silk.core.serialization.serializers

import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.silkmc.silk.core.serialization.SilkSerializer
import net.minecraft.resources.ResourceLocation

@Deprecated("Has been renamed to ResourceLocationSerializer", replaceWith = ReplaceWith("ResourceLocationSerializer"))
typealias IdentifierSerializer = ResourceLocationSerializer

class ResourceLocationSerializer : SilkSerializer<ResourceLocation>() {
    override fun deserialize(decoder: Decoder): ResourceLocation {
        val split = decoder.decodeString().split(':')
        return ResourceLocation(split[0], split[1])
    }

    override fun serialize(encoder: Encoder, value: ResourceLocation) {
        encoder.encodeString("${value.namespace}:${value.path}")
    }
}
