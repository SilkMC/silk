package net.axay.fabrik.nbt

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.property.Arb
import io.kotest.property.arbitrary.byte
import io.kotest.property.arbitrary.byteArrays
import io.kotest.property.arbitrary.int
import io.kotest.property.arbitrary.list
import io.kotest.property.checkAll
import kotlinx.serialization.Serializable
import kotlinx.serialization.serializer
import net.minecraft.nbt.NbtByteArray
import net.minecraft.nbt.NbtCompound

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

    "byte lists and arrays should encode to byte array" {
        checkAll(Arb.byteArrays(Arb.int(0..0x1000), Arb.byte())) {
            val element = Nbt.encodeToNbtElement(it)
            element.shouldBeInstanceOf<NbtByteArray>()
            element.byteArray shouldBe it
        }
        checkAll(Arb.list(Arb.byte(), 0..0x1000)) {
            val element = Nbt.encodeToNbtElement(it)
            element.shouldBeInstanceOf<NbtByteArray>()
            element.byteArray shouldBe it.toByteArray()
        }
    }
})

@Serializable
class TestClass(val x: Int, val y: Long, val name: String)
