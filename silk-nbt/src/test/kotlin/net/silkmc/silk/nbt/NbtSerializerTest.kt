@file:OptIn(ExperimentalSerializationApi::class)

package net.silkmc.silk.nbt

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.modules.plus
import net.minecraft.nbt.Tag
import net.silkmc.silk.core.serialization.SilkSerializer
import net.silkmc.silk.nbt.dsl.nbtCompound
import net.silkmc.silk.nbt.serialization.TagsModule
import net.silkmc.silk.nbt.serialization.serializer.*

val cbor = Cbor {
    serializersModule += TagsModule
}

private fun <Data, T : Tag, Deserializer : SilkSerializer<T>> Data.shouldSerializable(
    serializer: Deserializer,
    tag: Data.() -> T
) = cbor.decodeFromByteArray(serializer, cbor.encodeToByteArray(serializer, tag(this))) shouldBe tag(this)

class NbtSerializerTest : StringSpec({
    "primitive should serializable" {
        "foo".shouldSerializable(StringTagSerializer) { toNbt() }
        25565.toByte().shouldSerializable(ByteTagSerializer) { toNbt() }
        25565.toDouble().shouldSerializable(DoubleTagSerializer) { toNbt() }
        25565F.shouldSerializable(FloatTagSerializer) { toNbt() }
        25565.shouldSerializable(IntTagSerializer) { toNbt() }
        25565L.shouldSerializable(LongTagSerializer) { toNbt() }
        25565.toShort().shouldSerializable(ShortTagSerializer) { toNbt() }
    }

    "collection like should serializable" {
        byteArrayOf(0, 2, 4, 8).shouldSerializable(ByteArrayTagSerializer) { toNbt() }
        intArrayOf(0, 1, 2, 3).shouldSerializable(IntArrayTagSerializer) { toNbt() }
        longArrayOf(5, 6, 7, 8).shouldSerializable(LongArrayTagSerializer) { toNbt() }
        listOf("foo", "bar").shouldSerializable(ListTagSerializer) { map { it.toNbt() }.toNbt() }
    }

    "compound should serializable" {
        nbtCompound {
            put("foo", 0)
            compound("bar") {
                put("xxx", "abb")
            }
        }.shouldSerializable(CompoundTagSerializer) { this }
    }
})