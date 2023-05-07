package net.silkmc.silk.core.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlin.reflect.KClass

abstract class SilkSerializer<T : Any>(
    baseClass: KClass<T>
) : KSerializer<T> {
    val descriptorName = "SilkSerializer<${baseClass.simpleName}>"
}

abstract class SilkPrimitiveSerializer<T : Any>(
    val primitiveKind: PrimitiveKind = PrimitiveKind.STRING,
    baseClass: KClass<T>
) : SilkSerializer<T>(baseClass) {
    override val descriptor = PrimitiveSerialDescriptor(descriptorName, PrimitiveKind.STRING)
}