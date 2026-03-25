package net.silkmc.silk.core.serialization.serializers

import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.minecraft.resources.Identifier
import net.silkmc.silk.core.serialization.SilkSerializer

class IdentifierSerializer : SilkSerializer<Identifier>() {
    override fun deserialize(decoder: Decoder): Identifier {
        return Identifier.bySeparator(decoder.decodeString(), ':')
    }

    override fun serialize(encoder: Encoder, value: Identifier) {
        encoder.encodeString("${value.namespace}:${value.path}")
    }
}
