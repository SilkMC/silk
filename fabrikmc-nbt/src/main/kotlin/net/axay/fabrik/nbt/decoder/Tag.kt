package net.axay.fabrik.nbt.decoder

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import net.axay.fabrik.nbt.internal.*
import net.axay.fabrik.nbt.mixin.NbtCompoundAccessor
import net.minecraft.nbt.*

@ExperimentalSerializationApi
abstract class NbtTagDecoder(override val serializersModule: SerializersModule) : AbstractDecoder() {
    private enum class NextArrayType {
        Byte, Int, Long, None
    }

    private var nextArrayType = NextArrayType.None

    abstract fun next(): NbtElement

    @Suppress("unchecked_cast")
    override fun <T> decodeSerializableValue(deserializer: DeserializationStrategy<T>): T =
        when (deserializer) {
            byteArraySerializer -> decodeByteArray() as T
            intArraySerializer -> decodeIntArray() as T
            longArraySerializer -> decodeLongArray() as T
            else -> {
                nextArrayType = when (deserializer.elementSerializer) {
                    byteSerializer -> NextArrayType.Byte
                    intSerializer -> NextArrayType.Int
                    longSerializer -> NextArrayType.Long
                    else -> NextArrayType.None
                }
                super.decodeSerializableValue(deserializer)
            }
        }

    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder =
        when (nextArrayType) {
            NextArrayType.Byte -> NbtByteArrayDecoder(next() as NbtByteArray)
            NextArrayType.Int -> NbtIntArrayDecoder(next() as NbtIntArray)
            NextArrayType.Long -> NbtLongArrayDecoder(next() as NbtLongArray)
            NextArrayType.None -> when (descriptor.kind) {
                StructureKind.LIST -> NbtListDecoder(serializersModule, next() as NbtList)
                else -> NbtCompoundDecoder(serializersModule, next() as NbtCompound)
            }
        }

    override fun decodeBoolean(): Boolean =
        when ((next() as NbtByte).byteValue()) {
            0.toByte() -> false
            1.toByte() -> true
            else -> throw SerializationException("Byte is not a valid boolean")
        }

    override fun decodeByte(): Byte = (next() as NbtByte).byteValue()
    override fun decodeShort(): Short = (next() as NbtShort).shortValue()
    override fun decodeChar(): Char = (next() as NbtInt).intValue().toChar()
    override fun decodeInt(): Int = (next() as NbtInt).intValue()
    override fun decodeLong(): Long = (next() as NbtLong).longValue()
    override fun decodeFloat(): Float = (next() as NbtFloat).floatValue()
    override fun decodeDouble(): Double = (next() as NbtDouble).doubleValue()
    override fun decodeString(): String = (next() as NbtString).asString()
    override fun decodeEnum(enumDescriptor: SerialDescriptor): Int =
        enumDescriptor.getElementIndex(decodeString())

    private fun decodeByteArray(): ByteArray = (next() as NbtByteArray).byteArray
    private fun decodeIntArray(): IntArray = (next() as NbtIntArray).intArray
    private fun decodeLongArray(): LongArray = (next() as NbtLongArray).longArray
}

@ExperimentalSerializationApi
class NbtCompoundDecoder(
    serializersModule: SerializersModule,
    private val compound: NbtCompound
) : NbtTagDecoder(serializersModule) {
    private val entries = (compound as NbtCompoundAccessor).entries.iterator()
    private lateinit var currentEntry: Map.Entry<String, NbtElement>

    override fun next() = currentEntry.value

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int =
        if (entries.hasNext()) {
            currentEntry = entries.next()
            descriptor.getElementIndex(currentEntry.key)
        } else {
            CompositeDecoder.DECODE_DONE
        }

    override fun decodeCollectionSize(descriptor: SerialDescriptor) = compound.size
}

@ExperimentalSerializationApi
class NbtListDecoder(
    serializersModule: SerializersModule,
    private val list: NbtList
) : NbtTagDecoder(serializersModule) {
    private val elements = list.listIterator()

    override fun next(): NbtElement = elements.next()

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int =
        if (elements.hasNext()) {
            elements.nextIndex()
        } else {
            CompositeDecoder.DECODE_DONE
        }

    override fun decodeCollectionSize(descriptor: SerialDescriptor) = list.size
    override fun decodeSequentially() = true
}

@ExperimentalSerializationApi
class NbtByteArrayDecoder(array: NbtByteArray) : AbstractDecoder() {
    private val array = array.byteArray

    override val serializersModule: SerializersModule = EmptySerializersModule

    private var idx = 0

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int = idx
    override fun decodeCollectionSize(descriptor: SerialDescriptor) = array.size
    override fun decodeByte(): Byte = array[idx++]
    override fun decodeSequentially() = true
}

@ExperimentalSerializationApi
class NbtIntArrayDecoder(array: NbtIntArray) : AbstractDecoder() {
    private val array = array.intArray

    override val serializersModule: SerializersModule = EmptySerializersModule

    private var idx = 0

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int = idx
    override fun decodeCollectionSize(descriptor: SerialDescriptor) = array.size
    override fun decodeInt(): Int = array[idx++]
    override fun decodeSequentially() = true
}

@ExperimentalSerializationApi
class NbtLongArrayDecoder(array: NbtLongArray) : AbstractDecoder() {
    private val array = array.longArray

    override val serializersModule: SerializersModule = EmptySerializersModule

    private var idx = 0

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int = idx
    override fun decodeCollectionSize(descriptor: SerialDescriptor) = array.size
    override fun decodeLong(): Long = array[idx++]
    override fun decodeSequentially() = true
}
