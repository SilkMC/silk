package net.axay.fabrik.nbt.encoder

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.encoding.AbstractEncoder
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.serializer
import net.minecraft.nbt.*
import kotlin.collections.ArrayDeque

private val byteArraySerializer = serializer<ByteArray>()
private val byteListSerializer = serializer<List<Byte>>()
private val intArraySerializer = serializer<IntArray>()
private val intListSerializer = serializer<List<Int>>()
private val longArraySerializer = serializer<LongArray>()
private val longListSerializer = serializer<List<Long>>()

@OptIn(ExperimentalSerializationApi::class)
abstract class NbtTagEncoder : AbstractEncoder() {
    override val serializersModule: SerializersModule = EmptySerializersModule

    @Suppress("unchecked_cast")
    override fun <T> encodeSerializableValue(serializer: SerializationStrategy<T>, value: T) {
        when (serializer) {
            byteArraySerializer -> encodeByteArray(value as ByteArray)
            byteListSerializer -> encodeByteArray((value as List<Byte>).toByteArray())
            intArraySerializer -> encodeIntArray(value as IntArray)
            intListSerializer -> encodeIntArray((value as List<Int>).toIntArray())
            longArraySerializer -> encodeLongArray(value as LongArray)
            longListSerializer -> encodeLongArray((value as List<Long>).toLongArray())
            else -> super.encodeSerializableValue(serializer, value)
        }
    }

    open fun encodeByteArray(value: ByteArray) {
        throw SerializationException("Byte arrays are not supported by this encoder")
    }

    open fun encodeIntArray(value: IntArray) {
        throw SerializationException("Int arrays are not supported by this encoder")
    }

    open fun encodeLongArray(value: LongArray) {
        throw SerializationException("Long arrays are not supported by this encoder")
    }
}

@OptIn(ExperimentalSerializationApi::class)
class NbtCompoundEncoder(private val consumer: (NbtCompound) -> Unit) : NbtTagEncoder() {
    private val compound = NbtCompound()
    private val tags = ArrayDeque<String>()

    override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder =
        when (descriptor.kind) {
            StructureKind.LIST -> NbtListEncoder { compound.put(popTag(), it) }
            else -> NbtCompoundEncoder { compound.put(popTag(), it) }
        }

    override fun endStructure(descriptor: SerialDescriptor) {
        consumer(compound)
    }

    override fun encodeElement(descriptor: SerialDescriptor, index: Int): Boolean {
        tags.add(descriptor.getElementName(index))
        return true
    }

    override fun encodeBoolean(value: Boolean) {
        compound.putBoolean(popTag(), value)
    }

    override fun encodeByte(value: Byte) {
        compound.putByte(popTag(), value)
    }

    override fun encodeShort(value: Short) {
        compound.putShort(popTag(), value)
    }

    override fun encodeInt(value: Int) {
        compound.putInt(popTag(), value)
    }

    override fun encodeLong(value: Long) {
        compound.putLong(popTag(), value)
    }

    override fun encodeFloat(value: Float) {
        compound.putFloat(popTag(), value)
    }

    override fun encodeDouble(value: Double) {
        compound.putDouble(popTag(), value)
    }

    override fun encodeChar(value: Char) {
        compound.putInt(popTag(), value.code)
    }

    override fun encodeString(value: String) {
        compound.putString(popTag(), value)
    }

    override fun encodeEnum(enumDescriptor: SerialDescriptor, index: Int) {
        compound.putString(popTag(), enumDescriptor.getElementName(index))
    }

    override fun encodeByteArray(value: ByteArray) {
        compound.putByteArray(popTag(), value)
    }

    override fun encodeIntArray(value: IntArray) {
        compound.putIntArray(popTag(), value)
    }

    override fun encodeLongArray(value: LongArray) {
        compound.putLongArray(popTag(), value)
    }

    private fun popTag() = tags.removeLast()
}

@OptIn(ExperimentalSerializationApi::class)
class NbtListEncoder(private val consumer: (NbtList) -> Unit) : NbtTagEncoder() {
    private val list = NbtList()

    override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder =
        when (descriptor.kind) {
            StructureKind.LIST -> NbtListEncoder { list.add(it) }
            else -> NbtCompoundEncoder { list.add(it) }
        }

    override fun endStructure(descriptor: SerialDescriptor) {
        consumer(list)
    }

    override fun encodeBoolean(value: Boolean) {
        list.add(NbtByte.of(value))
    }

    override fun encodeByte(value: Byte) {
        list.add(NbtByte.of(value))
    }

    override fun encodeShort(value: Short) {
        list.add(NbtShort.of(value))
    }

    override fun encodeInt(value: Int) {
        list.add(NbtInt.of(value))
    }

    override fun encodeLong(value: Long) {
        list.add(NbtLong.of(value))
    }

    override fun encodeFloat(value: Float) {
        list.add(NbtFloat.of(value))
    }

    override fun encodeDouble(value: Double) {
        list.add(NbtDouble.of(value))
    }

    override fun encodeChar(value: Char) {
        list.add(NbtInt.of(value.code))
    }

    override fun encodeString(value: String) {
        list.add(NbtString.of(value))
    }

    override fun encodeEnum(enumDescriptor: SerialDescriptor, index: Int) {
        list.add(NbtString.of(enumDescriptor.getElementName(index)))
    }

    override fun encodeByteArray(value: ByteArray) {
        list.add(NbtByteArray(value))
    }

    override fun encodeIntArray(value: IntArray) {
        list.add(NbtIntArray(value))
    }

    override fun encodeLongArray(value: LongArray) {
        list.add(NbtLongArray(value))
    }
}
