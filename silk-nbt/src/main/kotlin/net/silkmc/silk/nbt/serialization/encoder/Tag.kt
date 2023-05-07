package net.silkmc.silk.nbt.serialization.encoder

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationStrategy
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.encoding.AbstractEncoder
import kotlinx.serialization.encoding.CompositeEncoder
import kotlinx.serialization.modules.SerializersModule
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.StringTag
import net.minecraft.nbt.Tag
import net.silkmc.silk.nbt.serialization.Nbt
import net.silkmc.silk.nbt.serialization.internal.*
import net.silkmc.silk.nbt.toNbt


@ExperimentalSerializationApi
@Deprecated(message = "Renamed by mojmap", replaceWith = ReplaceWith("TagEncoder"))
typealias NbtTagEncoder = TagEncoder

@ExperimentalSerializationApi
@Deprecated(message = "Renamed by mojmap", replaceWith = ReplaceWith("RootTagEncoder"))
typealias NbtRootEncoder = RootTagEncoder

@ExperimentalSerializationApi
@Deprecated(message = "Renamed by mojmap", replaceWith = ReplaceWith("CompoundTagEncoder"))
typealias NbtCompoundEncoder = CompoundTagEncoder

@ExperimentalSerializationApi
@Deprecated(message = "Renamed by mojmap", replaceWith = ReplaceWith("ListTagEncoder"))
typealias NbtListEncoder = ListTagEncoder

@ExperimentalSerializationApi
abstract class TagEncoder(protected val nbt: Nbt) : AbstractEncoder() {
    override val serializersModule: SerializersModule = nbt.serializersModule

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

    private fun encodeMaybeNullable(element: Tag) {
        encodeElement(if (isNextNullable) {
            isNextNullable = false
            // Always use a list because we cannot know whether a null value would be one of the primitives
            ListTag().apply { add(element) }
        } else {
            element
        })
    }

    abstract fun encodeElement(element: Tag)

    abstract fun consumeStructure(element: Tag)

    override fun beginStructure(descriptor: SerialDescriptor): CompositeEncoder =
        when (descriptor.kind) {
            StructureKind.LIST -> ListTagEncoder(nbt, ::consumeStructure)
            StructureKind.MAP -> CompoundTagMapEncoder(nbt, ::consumeStructure)
            else -> CompoundTagEncoder(nbt, ::consumeStructure)
        }

    override fun encodeNotNullMark() {
        isNextNullable = true
    }

    override fun encodeNull() {
        encodeElement(ListTag())
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
        encodeMaybeNullable(StringTag.valueOf(enumDescriptor.getElementName(index)))
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
class RootTagEncoder(nbt: Nbt) : TagEncoder(nbt) {
    var element: Tag? = null
        private set

    override fun encodeElement(element: Tag) {
        this.element = element
    }

    override fun consumeStructure(element: Tag) {
        this.element = element
    }
}


@ExperimentalSerializationApi
class CompoundTagMapEncoder(
    nbt: Nbt,
    private val consumer: (CompoundTag) -> Unit
) : TagEncoder(nbt) {
    private val compound = CompoundTag()
    private lateinit var key: String
    private var isKey = true

    override fun encodeElement(element: Tag) {
        if (isKey) { // writing key
            key = element.asString
            isKey = false
        } else {
            compound.put(key, element)
            isKey = true
        }
    }

    override fun consumeStructure(element: Tag) = encodeElement(element)

    override fun endStructure(descriptor: SerialDescriptor) {
        consumer(compound)
    }

    override fun shouldEncodeElementDefault(descriptor: SerialDescriptor, index: Int) =
        nbt.config.encodeDefaults
}

@ExperimentalSerializationApi
class CompoundTagEncoder(
    nbt: Nbt,
    private val consumer: (CompoundTag) -> Unit
) : TagEncoder(nbt) {
    private val compound = CompoundTag()
    private val tags = ArrayDeque<String>()

    override fun encodeElement(descriptor: SerialDescriptor, index: Int): Boolean {
        tags.add(descriptor.getElementName(index))
        return true
    }

    override fun encodeElement(element: Tag) {
        compound.put(popTag(), element)
    }

    override fun consumeStructure(element: Tag) {
        compound.put(popTag(), element)
    }

    override fun endStructure(descriptor: SerialDescriptor) {
        consumer(compound)
    }

    override fun shouldEncodeElementDefault(descriptor: SerialDescriptor, index: Int) =
        nbt.config.encodeDefaults

    private fun popTag() = tags.removeLast()
}

@ExperimentalSerializationApi
class ListTagEncoder(
    nbt: Nbt,
    private val consumer: (ListTag) -> Unit
) : TagEncoder(nbt) {
    private val list = ListTag()

    override fun encodeElement(element: Tag) {
        list.add(element)
    }

    override fun consumeStructure(element: Tag) {
        list.add(element)
    }

    override fun endStructure(descriptor: SerialDescriptor) {
        consumer(list)
    }
}

