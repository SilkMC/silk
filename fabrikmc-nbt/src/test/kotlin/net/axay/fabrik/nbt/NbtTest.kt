package net.axay.fabrik.nbt

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.property.Arb
import io.kotest.property.arbitrary.*
import io.kotest.property.checkAll
import kotlinx.serialization.Serializable
import net.minecraft.nbt.NbtByteArray
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtIntArray
import net.minecraft.nbt.NbtLongArray

class NbtTest : StringSpec({
    "class should encode to compound" {
        val value = TestClass(10, -1248, "Taito")
        val element = Nbt.encodeToNbtElement(value)
        element.shouldBeInstanceOf<NbtCompound>()
        element.size shouldBe 3
        element.getInt("x") shouldBe value.x
        element.getLong("y") shouldBe value.y
        element.getString("name") shouldBe value.name
    }

    "byte arrays and lists should encode to byte array" {
        checkAll(Arb.byteArrays(Arb.int(0..0x1000), Arb.byte())) {
            val element = Nbt.encodeToNbtElement(it)
            element.shouldBeInstanceOf<NbtByteArray>()
            element.byteArray shouldBe it
        }
        checkAll(Arb.list(Arb.byte(), 1..0x1000)) {
            val element = Nbt.encodeToNbtElement(it)
            element.shouldBeInstanceOf<NbtByteArray>()
            element.byteArray shouldBe it.toByteArray()
        }
    }

    "int arrays and lists should encode to byte array" {
        checkAll(Arb.list(Arb.int(), 0..0x1000)) {
            val array = it.toIntArray()
            val elements = listOf(Nbt.encodeToNbtElement(it), Nbt.encodeToNbtElement(array))
            for (element in elements) {
                element.shouldBeInstanceOf<NbtIntArray>()
                element.intArray shouldBe array
            }
        }
    }

    "long arrays and lists should encode to byte array" {
        checkAll(Arb.list(Arb.long(), 0..0x1000)) {
            val array = it.toLongArray()
            val elements = listOf(Nbt.encodeToNbtElement(it), Nbt.encodeToNbtElement(array))
            for (element in elements) {
                element.shouldBeInstanceOf<NbtLongArray>()
                element.longArray shouldBe array
            }
        }
    }
})

@Serializable
class TestClass(val x: Int, val y: Long, val name: String)
