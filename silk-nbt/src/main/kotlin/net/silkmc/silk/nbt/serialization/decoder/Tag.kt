package net.silkmc.silk.nbt.serialization.decoder

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.StructureKind
import kotlinx.serialization.encoding.AbstractDecoder
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.modules.EmptySerializersModule
import kotlinx.serialization.modules.SerializersModule
import net.minecraft.nbt.*
import net.silkmc.silk.nbt.serialization.Nbt
import net.silkmc.silk.nbt.serialization.UnknownKeyException
import net.silkmc.silk.nbt.serialization.internal.*
import net.silkmc.silk.nbt.serialization.serializer.ByteTagSerializer
import net.silkmc.silk.nbt.serialization.serializer.IntTagSerializer
import net.silkmc.silk.nbt.serialization.serializer.LongTagSerializer
import net.silkmc.silk.nbt.toNbt

@ExperimentalSerializationApi
@Deprecated(message = "Renamed by mojmap", replaceWith = ReplaceWith("TagDecoder"))
typealias NbtTagDecoder = TagDecoder

@ExperimentalSerializationApi
@Deprecated(message = "Renamed by mojmap", replaceWith = ReplaceWith("RootTagDecoder"))
typealias NbtRootDecoder = RootTagDecoder

@ExperimentalSerializationApi
@Deprecated(message = "Renamed by mojmap", replaceWith = ReplaceWith("CompoundTagDecoder"))
typealias NbtCompoundDecoder = CompoundTagDecoder

@ExperimentalSerializationApi
@Deprecated(message = "Renamed by mojmap", replaceWith = ReplaceWith("ListTagDecoder"))
typealias NbtListDecoder = ListTagDecoder

@ExperimentalSerializationApi
@Deprecated(message = "Renamed by mojmap", replaceWith = ReplaceWith("ByteArrayTagDecoder"))
typealias NbtByteArrayDecoder = ByteArrayTagDecoder

@ExperimentalSerializationApi
@Deprecated(message = "Renamed by mojmap", replaceWith = ReplaceWith("IntArrayTagDecoder"))
typealias NbtIntArrayDecoder = IntArrayTagDecoder

@ExperimentalSerializationApi
@Deprecated(message = "Renamed by mojmap", replaceWith = ReplaceWith("LongArrayTagDecoder"))
typealias NbtLongArrayDecoder = LongArrayTagDecoder

@ExperimentalSerializationApi
abstract class TagDecoder(protected val nbt: Nbt) : AbstractDecoder() {
    override val serializersModule: SerializersModule = nbt.serializersModule

    private enum class NextArrayType {
        Byte, Int, Long, None
    }

    private var nextArrayType = NextArrayType.None
    private var nextNullable: Tag? = null

    abstract fun next(): Tag

    @Suppress("unchecked_cast")
    override fun <T> decodeSerializableValue(deserializer: DeserializationStrategy<T>): T =
        when (deserializer) {
            byteArraySerializer -> decodeByteArray() as T
            intArraySerializer -> decodeIntArray() as T
            longArraySerializer -> decodeLongArray() as T
            else -> {
                nextArrayType = when (deserializer.elementSerializer) {
                    byteSerializer, ByteTagSerializer -> NextArrayType.Byte
                    intSerializer, IntTagSerializer -> NextArrayType.Int
                    longSerializer, LongTagSerializer -> NextArrayType.Long
                    else -> NextArrayType.None
                }
                super.decodeSerializableValue(deserializer)
            }
        }

    override fun beginStructure(descriptor: SerialDescriptor): CompositeDecoder =
        when (nextArrayType) {
            NextArrayType.Byte -> ByteArrayTagDecoder(nextMaybeNullable() as ByteArrayTag)
            NextArrayType.Int -> IntArrayTagDecoder(nextMaybeNullable() as IntArrayTag)
            NextArrayType.Long -> LongArrayTagDecoder(nextMaybeNullable() as LongArrayTag)
            NextArrayType.None -> when (descriptor.kind) {
                StructureKind.LIST -> ListTagDecoder(nbt, nextMaybeNullable() as ListTag)
                StructureKind.MAP -> CompoundTagMapDecoder(nbt, nextMaybeNullable() as CompoundTag)
                else -> CompoundTagDecoder(nbt, nextMaybeNullable() as CompoundTag)
            }
        }

    override fun decodeNotNullMark() =
        (next() as ListTag).let {
            if (it.isEmpty()) {
                false
            } else {
                nextNullable = it[0]
                true
            }
        }

    override fun decodeNull(): Nothing? {
        nextNullable = null
        return null
    }

    fun nextMaybeNullable(): Tag {
        val next = nextNullable ?: next()
        nextNullable = null
        return next
    }

    override fun decodeBoolean(): Boolean =
        when ((nextMaybeNullable() as ByteTag).asByte) {
            0.toByte() -> false
            1.toByte() -> true
            else -> throw SerializationException("Byte is not a valid boolean")
        }

    override fun decodeByte(): Byte = (nextMaybeNullable() as ByteTag).asByte
    override fun decodeShort(): Short = (nextMaybeNullable() as ShortTag).asShort
    override fun decodeChar(): Char = (nextMaybeNullable() as IntTag).asInt.toChar()
    override fun decodeInt(): Int = (nextMaybeNullable() as IntTag).asInt
    override fun decodeLong(): Long = (nextMaybeNullable() as LongTag).asLong
    override fun decodeFloat(): Float = (nextMaybeNullable() as FloatTag).asFloat
    override fun decodeDouble(): Double = (nextMaybeNullable() as DoubleTag).asDouble
    override fun decodeString(): String = (nextMaybeNullable() as StringTag).asString
    override fun decodeEnum(enumDescriptor: SerialDescriptor): Int =
        enumDescriptor.getElementIndex(decodeString())

    private fun decodeByteArray(): ByteArray = (nextMaybeNullable() as ByteArrayTag).asByteArray
    private fun decodeIntArray(): IntArray = (nextMaybeNullable() as IntArrayTag).asIntArray
    private fun decodeLongArray(): LongArray = (nextMaybeNullable() as LongArrayTag).asLongArray
}

@ExperimentalSerializationApi
class RootTagDecoder(
    nbt: Nbt,
    private val element: Tag
) : TagDecoder(nbt) {
    override fun next() = element

    override fun decodeElementIndex(descriptor: SerialDescriptor) = 0
}

@ExperimentalSerializationApi
class CompoundTagMapDecoder(
    nbt: Nbt,
    private val compound: CompoundTag
) : TagDecoder(nbt) {
    private var idx = -1
    private val iterator = compound.allKeys.iterator()
    private var isKey = true
    private lateinit var key: String

    override fun next() = if (isKey) {
        key = iterator.next()
        isKey = false
        key.toNbt()
    } else {
        isKey = true
        compound.get(key)!!
    }

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        while (idx++ < descriptor.elementsCount * 2 - 1) {
            return idx
        }
        return CompositeDecoder.DECODE_DONE
    }

    override fun decodeCollectionSize(descriptor: SerialDescriptor) = compound.size()

    override fun endStructure(descriptor: SerialDescriptor) {
        // do nothing, maps do not have strict keys, so strict mode check is omitted
    }
}

@ExperimentalSerializationApi
class CompoundTagDecoder(
    nbt: Nbt,
    private val compound: CompoundTag
) : TagDecoder(nbt) {
    private lateinit var element: Tag
    private var idx = 0

    override fun next() = element

    override fun decodeElementIndex(descriptor: SerialDescriptor): Int {
        while (idx < descriptor.elementsCount) {
            val name = descriptor.getElementName(idx++)
            compound.get(name)?.let {
                element = it
                return idx - 1
            }
        }
        return CompositeDecoder.DECODE_DONE
    }

    override fun decodeCollectionSize(descriptor: SerialDescriptor) = compound.size()

    override fun endStructure(descriptor: SerialDescriptor) {
        if (nbt.config.ignoreUnknownKeys || descriptor.kind is PolymorphicKind) return

        val names = (0 until descriptor.elementsCount).mapTo(HashSet()) { descriptor.getElementName(it) }
        for (key in compound.allKeys) {
            if (!names.contains(key)) {
                throw UnknownKeyException(key)
            }
        }
    }
}

@ExperimentalSerializationApi
class ListTagDecoder(
    nbt: Nbt,
    private val list: ListTag
) : TagDecoder(nbt) {
    private val elements = list.listIterator()

    override fun next(): Tag = elements.next()

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
abstract class CollectionTagDecoder(
    override val serializersModule: SerializersModule = EmptySerializersModule()
) : AbstractDecoder() {
    protected var idx = 0
    override fun decodeElementIndex(descriptor: SerialDescriptor): Int = idx

    override fun decodeSequentially() = true
}

@ExperimentalSerializationApi
class ByteArrayTagDecoder(
    array: ByteArrayTag
) : CollectionTagDecoder() {
    private val array = array.asByteArray
    override fun decodeCollectionSize(descriptor: SerialDescriptor) = array.size
    override fun decodeByte(): Byte = array[idx++]
}

@ExperimentalSerializationApi
class IntArrayTagDecoder(
    array: IntArrayTag
) : CollectionTagDecoder() {
    private val array = array.asIntArray
    override fun decodeCollectionSize(descriptor: SerialDescriptor) = array.size
    override fun decodeInt(): Int = array[idx++]
}

@ExperimentalSerializationApi
class LongArrayTagDecoder(
    array: LongArrayTag
) : CollectionTagDecoder() {
    private val array = array.asLongArray
    override fun decodeCollectionSize(descriptor: SerialDescriptor) = array.size
    override fun decodeLong(): Long = array[idx++]
}
