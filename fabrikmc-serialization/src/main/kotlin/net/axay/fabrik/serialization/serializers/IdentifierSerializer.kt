package net.axay.fabrik.serialization.serializers

import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.axay.fabrik.serialization.FabrikSerializer
import net.minecraft.util.Identifier

class IdentifierSerializer : FabrikSerializer<Identifier>() {
    override fun deserialize(decoder: Decoder): Identifier {
        val split = decoder.decodeString().split(':')
        return Identifier(split[0], split[1])
    }

    override fun serialize(encoder: Encoder, value: Identifier) {
        encoder.encodeString("${value.namespace}:${value.path}")
    }
}
