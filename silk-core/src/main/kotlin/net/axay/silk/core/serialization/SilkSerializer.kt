package net.axay.silk.core.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor

abstract class SilkSerializer<T>(
    val primitiveKind: PrimitiveKind = PrimitiveKind.STRING
) : KSerializer<T> {
    val descriptorName = javaClass.name
    override val descriptor = PrimitiveSerialDescriptor(descriptorName, PrimitiveKind.STRING)
}
