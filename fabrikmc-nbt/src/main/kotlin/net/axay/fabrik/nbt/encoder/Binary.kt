package net.axay.fabrik.nbt.encoder

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encoding.AbstractEncoder
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule

@ExperimentalSerializationApi
class BinaryNbtEncoder : AbstractEncoder() {
    override val serializersModule: SerializersModule = EmptySerializersModule
}
