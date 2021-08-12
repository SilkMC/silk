package net.axay.fabrik.nbt

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.SerialKind
import kotlinx.serialization.descriptors.StructureKind
import net.axay.fabrik.nbt.encoder.NbtCompoundEncoder
import net.axay.fabrik.nbt.encoder.NbtListEncoder
import net.axay.fabrik.nbt.encoder.NbtPrimitiveEncoder
import net.minecraft.nbt.NbtElement

sealed class Nbt {
    companion object Default : Nbt()

    @OptIn(ExperimentalSerializationApi::class)
    fun <T> encodeToNbtElement(serializer: SerializationStrategy<T>, value: T): NbtElement =
        when (val kind = serializer.descriptor.kind) {
            is PrimitiveKind, SerialKind.ENUM -> {
                val encoder = NbtPrimitiveEncoder()
                encoder.encodeSerializableValue(serializer, value)
                encoder.value ?: throw SerializationException("Primitive NBT value has not been recorded")
            }
            StructureKind.LIST -> {
                lateinit var element: NbtElement
                NbtListEncoder { element = it }.encodeSerializableValue(serializer, value)
                element
            }
            else -> {
                lateinit var element: NbtElement
                val encoder = if (kind == StructureKind.LIST) {
                    NbtListEncoder { element = it }
                } else {
                    NbtCompoundEncoder { element = it }
                }
                encoder.encodeSerializableValue(serializer, value)
                encoder.endStructure(serializer.descriptor)
                element
            }
        }

    fun <T> decodeFromNbtElement(deserializer: DeserializationStrategy<T>, element: NbtElement) {

    }
}
