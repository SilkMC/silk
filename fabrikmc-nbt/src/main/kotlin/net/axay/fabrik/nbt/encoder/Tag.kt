package net.axay.fabrik.nbt.encoder

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.encoding.AbstractEncoder
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import net.axay.fabrik.nbt.internal.*
import net.axay.fabrik.nbt.toNbt
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtList
import net.minecraft.nbt.NbtString

@ExperimentalSerializationApi
abstract class NbtTagEncoder : AbstractEncoder() {
    override val serializersModule: SerializersModule = EmptySerializersModule

    override fun <T> encodeSerializableValue(serializer: SerializationStrategy<T>, value: T) {
        when (serializer) {
            byteArraySerializer -> encodeByteArray(value as ByteArray)
            intArraySerializer -> encodeIntArray(value as IntArray)
            longArraySerializer -> encodeLongArray(value as LongArray)
            else ->
                @Suppress("unchecked_cast")
                when (serializer.elementSerializer) {
                    byteSerializer -> encodeByteArray((value as Collection<Byte>).toByteArray())
                    intSerializer -> encodeIntArray((value as Collection<Int>).toIntArray())
                    longSerializer -> encodeLongArray((value as Collection<Long>).toLongArray())
                    else -> super.encodeSerializableValue(serializer, value)
                }
        }
    }

    abstract fun encodeElement(element: NbtElement)

    abstract fun consumeStructure(element: NbtElement)

    override fun encodeBoolean(value: Boolean) {
        encodeElement(value.toNbt())
    }

    override fun encodeByte(value: Byte) {
        encodeElement(value.toNbt())
    }

    override fun encodeShort(value: Short) {
        encodeElement(value.toNbt())
    }

    override fun encodeInt(value: Int) {
        encodeElement(value.toNbt())
    }

    override fun encodeLong(value: Long) {
        encodeElement(value.toNbt())
    }

    override fun encodeFloat(value: Float) {
        encodeElement(value.toNbt())
    }

    override fun encodeDouble(value: Double) {
        encodeElement(value.toNbt())
    }

    override fun encodeChar(value: Char) {
        encodeElement(value.toNbt())
    }

    override fun encodeString(value: String) {
        encodeElement(value.toNbt())
    }

    override fun encodeEnum(enumDescriptor: SerialDescriptor, index: Int) {
        encodeElement(NbtString.of(enumDescriptor.getElementName(index)))
    }

    private fun encodeByteArray(value: ByteArray) {
        encodeElement(value.toNbt())
    }

    private fun encodeIntArray(value: IntArray) {
        encodeElement(value.toNbt())
    }

    private fun encodeLongArray(value: LongArray) {
        encodeElement(value.toNbt())
    }

    override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder =
        when (descriptor.kind) {
            StructureKind.LIST -> NbtListEncoder(::consumeStructure)
            else -> NbtCompoundEncoder(::consumeStructure)
        }
}

@ExperimentalSerializationApi
class NbtRootEncoder : NbtTagEncoder() {
    var element: NbtElement? = null
        private set

    override fun encodeElement(element: NbtElement) {
        this.element = element
    }

    override fun consumeStructure(element: NbtElement) {
        this.element = element
    }
}

@ExperimentalSerializationApi
class NbtCompoundEncoder(private val consumer: (NbtCompound) -> Unit) : NbtTagEncoder() {
    private val compound = NbtCompound()
    private val tags = ArrayDeque<String>()

    override fun encodeElement(descriptor: SerialDescriptor, index: Int): Boolean {
        tags.add(descriptor.getElementName(index))
        return true
    }

    override fun encodeElement(element: NbtElement) {
        compound.put(popTag(), element)
    }

    override fun consumeStructure(element: NbtElement) {
        compound.put(popTag(), element)
    }

    override fun endStructure(descriptor: SerialDescriptor) {
        consumer(compound)
    }

    private fun popTag() = tags.removeLast()
}

@ExperimentalSerializationApi
class NbtListEncoder(private val consumer: (NbtList) -> Unit) : NbtTagEncoder() {
    private val list = NbtList()

    override fun encodeElement(element: NbtElement) {
        list.add(element)
    }

    override fun consumeStructure(element: NbtElement) {
        list.add(element)
    }

    override fun endStructure(descriptor: SerialDescriptor) {
        consumer(list)
    }
}
