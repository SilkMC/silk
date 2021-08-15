package net.axay.fabrik.nbt.decoder

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import net.axay.fabrik.nbt.internal.byteArraySerializer
import net.axay.fabrik.nbt.internal.elementSerializer
import net.axay.fabrik.nbt.internal.intArraySerializer
import net.axay.fabrik.nbt.internal.longArraySerializer
import net.axay.fabrik.nbt.mixin.NbtCompoundAccessor
import net.minecraft.nbt.*

@ExperimentalSerializationApi
abstract class NbtTagDecoder : AbstractDecoder() {
    override val serializersModule: SerializersModule = EmptySerializersModule

    override fun <T> decodeSerializableValue(deserializer: DeserializationStrategy<T>): T =
        when (deserializer) {
            byteArraySerializer -> decodeByteArray()
            intArraySerializer -> decodeIntArray()
            longArraySerializer -> decodeLongArray()
            else -> when (deserializer.elementSerializer) {
                else -> super.decodeSerializableValue(deserializer)
            }
        }

    private fun <T> decodeByteArray(): T {
        TODO()
    }

    private fun <T> decodeIntArray(): T {
        TODO()
    }

    private fun <T> decodeLongArray(): T {
        TODO()
    }
}

@ExperimentalSerializationApi
class NbtCompoundDecoder(compound: NbtCompound) : NbtTagDecoder() {
    private val entries = (compound as NbtCompoundAccessor).entries.iterator()
    private lateinit var current: Map.Entry<String, NbtElement>

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int =
        if (entries.hasNext()) {
            current = entries.next()
            descriptor.getElementIndex(current.key)
        } else {
            CompositeDecoder.DECODE_DONE
        }

    override fun decodeCollectionSize(descriptor: SerialDescriptor): Int =
        when (val element = current.value) {
            is NbtCompound -> element.size
            is AbstractNbtList<*> -> element.size
            else -> -1
        }

    override fun decodeBoolean(): Boolean =
        when ((current as NbtByte).byteValue()) {
            0.toByte() -> false
            1.toByte() -> true
            else -> throw SerializationException("Byte is not a valid boolean")
        }

    override fun decodeByte(): Byte = (current as NbtByte).byteValue()
    override fun decodeShort(): Short = (current as NbtShort).shortValue()
    override fun decodeChar(): Char = (current as NbtInt).intValue().toChar()
    override fun decodeInt(): Int = (current as NbtInt).intValue()
    override fun decodeLong(): Long = (current as NbtLong).longValue()
    override fun decodeFloat(): Float = (current as NbtFloat).floatValue()
    override fun decodeDouble(): Double = (current as NbtDouble).doubleValue()
    override fun decodeString(): String = (current as NbtString).asString()
    override fun decodeEnum(enumDescriptor: SerialDescriptor): Int =
        enumDescriptor.getElementIndex(decodeString())
}

@ExperimentalSerializationApi
class NbtListDecoder : NbtTagDecoder() {
    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        TODO("Not yet implemented")
    }
}
