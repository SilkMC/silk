package net.axay.fabrik.nbt.encoder

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.encoding.AbstractEncoder
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.modules.SerializersModule
import net.axay.fabrik.nbt.internal.*
import net.axay.fabrik.nbt.toNbt
import net.minecraft.nbt.*

@ExperimentalSerializationApi
abstract class NbtTagEncoder(override val serializersModule: SerializersModule) : AbstractEncoder() {
    private var isNextNullable = false

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

    private fun encodeMaybeNullable(element: NbtElement) {
        encodeElement(if (isNextNullable) {
            isNextNullable = false
            // Always use a list because we cannot know whether a null value would be one of the primitives
            NbtList().apply { add(element) }
        } else {
            element
        })
    }

    abstract fun encodeElement(element: NbtElement)

    abstract fun consumeStructure(element: NbtElement)

    override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder =
        when (descriptor.kind) {
            StructureKind.LIST -> NbtListEncoder(serializersModule, ::consumeStructure)
            else -> NbtCompoundEncoder(serializersModule, ::consumeStructure)
        }

    override fun encodeNotNullMark() {
        isNextNullable = true
    }

    override fun encodeNull() {
        encodeElement(NbtList())
    }

    override fun encodeBoolean(value: Boolean) {
        encodeMaybeNullable(value.toNbt())
    }

    override fun encodeByte(value: Byte) {
        encodeMaybeNullable(value.toNbt())
    }

    override fun encodeShort(value: Short) {
        encodeMaybeNullable(value.toNbt())
    }

    override fun encodeInt(value: Int) {
        encodeMaybeNullable(value.toNbt())
    }

    override fun encodeLong(value: Long) {
        encodeMaybeNullable(value.toNbt())
    }

    override fun encodeFloat(value: Float) {
        encodeMaybeNullable(value.toNbt())
    }

    override fun encodeDouble(value: Double) {
        encodeMaybeNullable(value.toNbt())
    }

    override fun encodeChar(value: Char) {
        encodeMaybeNullable(value.toNbt())
    }

    override fun encodeString(value: String) {
        encodeMaybeNullable(value.toNbt())
    }

    override fun encodeEnum(enumDescriptor: SerialDescriptor, index: Int) {
        encodeMaybeNullable(NbtString.of(enumDescriptor.getElementName(index)))
    }

    private fun encodeByteArray(value: ByteArray) {
        encodeMaybeNullable(value.toNbt())
    }

    private fun encodeIntArray(value: IntArray) {
        encodeMaybeNullable(value.toNbt())
    }

    private fun encodeLongArray(value: LongArray) {
        encodeMaybeNullable(value.toNbt())
    }
}

@ExperimentalSerializationApi
class NbtRootEncoder(serializersModule: SerializersModule) : NbtTagEncoder(serializersModule) {
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
class NbtCompoundEncoder(
    serializersModule: SerializersModule,
    private val consumer: (NbtCompound) -> Unit
) : NbtTagEncoder(serializersModule) {
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
class NbtListEncoder(
    serializersModule: SerializersModule,
    private val consumer: (NbtList) -> Unit
) : NbtTagEncoder(serializersModule) {
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
