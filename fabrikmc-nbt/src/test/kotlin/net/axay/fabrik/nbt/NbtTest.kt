package net.axay.fabrik.nbt

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import kotlinx.serialization.Serializable
import net.minecraft.nbt.NbtCompound

class NbtTest : StringSpec({
    "class should encode to NBT compound" {
        val value = TestClass(10, -1248, "Taito")
        val element = Nbt.encodeToNbtElement(value)
        element.shouldBeInstanceOf<NbtCompound>()
        element.size shouldBe 3
        element.getInt("x") shouldBe value.x
        element.getLong("y") shouldBe value.y
        element.getString("name") shouldBe value.name
    }
})

@Serializable
class TestClass(val x: Int, val y: Long, val name: String)
