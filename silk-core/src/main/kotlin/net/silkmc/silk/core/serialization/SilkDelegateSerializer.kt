package net.silkmc.silk.core.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

abstract class SilkDelegateSerializer<T, D>(
    private val delegateSerializer: KSerializer<D>,
    private val convertTo: T.() -> D,
    private val convertFrom: D.() -> T,
) : KSerializer<T> {

    override val descriptor: SerialDescriptor
        get() = delegateSerializer.descriptor

    override fun deserialize(decoder: Decoder): T {
        return decoder.decodeSerializableValue(delegateSerializer).convertFrom()
    }

    override fun serialize(encoder: Encoder, value: T) {
        encoder.encodeSerializableValue(delegateSerializer, value.convertTo())
    }
}
