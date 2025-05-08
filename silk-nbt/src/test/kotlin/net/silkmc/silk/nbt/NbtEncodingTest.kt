package net.silkmc.silk.nbt

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.property.Arb
import io.kotest.property.Exhaustive
import io.kotest.property.arbitrary.*
import io.kotest.property.checkAll
import io.kotest.property.exhaustive.enum
import net.minecraft.nbt.*
import net.silkmc.silk.nbt.serialization.Nbt
import net.silkmc.silk.nbt.serialization.encodeToNbtElement

class NbtEncodingTest : StringSpec({
    "class should encode to compound" {
        val value = TestClass(
            10,
            -1248,
            "Joe",
            listOf("abc", "äèðßèäßð", "1 2"),
            setOf(5, 34, 9, 3443),
            InnerTestClass(true),
            null,
        )
        val element = Nbt.encodeToNbtElement(value)
        element.shouldBeInstanceOf<CompoundTag>()
        element.size() shouldBe 7
        element.getInt("x").get() shouldBe value.x
        element.getLong("y").get() shouldBe value.y
        element.getString("name").get() shouldBe value.name
        element.getList("stringList").get().map { it.asString().get() } shouldBe value.stringList
        element.getLongArray("longSet").get() shouldBe value.longSet.toLongArray()
        with(element.getCompound("inner").get()) {
            size() shouldBe 1
            getBoolean("test").get() shouldBe value.inner.test
        }
        with(element.get("nullable")) {
            shouldBeInstanceOf<ListTag>()
            shouldBeEmpty()
        }
    }

    "nullable types should encode to lists" {
        with(Nbt.encodeToNbtElement<Int?>(null)) {
            shouldBeInstanceOf<ListTag>()
            shouldBeEmpty()
        }
        with(Nbt.encodeToNbtElement<Int?>(5)) {
            shouldBeInstanceOf<ListTag>()
            shouldHaveSize(1)
            this[0] shouldBe 5.toNbt()
        }
    }

    "byte collections should encode to byte array" {
        checkAll(Arb.byteArray(Arb.int(0..0x1000), Arb.byte())) {
            val element = Nbt.encodeToNbtElement(it)
            element.shouldBeInstanceOf<ByteArrayTag>()
            element.asByteArray shouldBe it
        }
        checkAll(Arb.list(Arb.byte(), 0..0x1000)) {
            val element = Nbt.encodeToNbtElement(it)
            element.shouldBeInstanceOf<ByteArrayTag>()
            element.asByteArray shouldBe it
        }
        checkAll(Arb.set(Arb.byte(), 0..Byte.MAX_VALUE)) {
            val element = Nbt.encodeToNbtElement(it)
            element.shouldBeInstanceOf<ByteArrayTag>()
            element.asByteArray shouldBe it.toByteArray()
        }
    }

    "int collections should encode to int array" {
        checkAll(Arb.list(Arb.int(), 0..0x1000)) {
            val array = it.toIntArray()
            val elements = listOf(Nbt.encodeToNbtElement(it), Nbt.encodeToNbtElement(array))
            for (element in elements) {
                element.shouldBeInstanceOf<IntArrayTag>()
                element.asIntArray shouldBe array
            }
        }
        checkAll(Arb.set(Arb.int(), 0..0x1000)) {
            val element = Nbt.encodeToNbtElement(it)
            element.shouldBeInstanceOf<IntArrayTag>()
            element.asIntArray shouldBe it.toIntArray()
        }
    }

    "long collections should encode to long array" {
        checkAll(Arb.list(Arb.long(), 0..0x1000)) {
            val array = it.toLongArray()
            val elements = listOf(Nbt.encodeToNbtElement(it), Nbt.encodeToNbtElement(array))
            for (element in elements) {
                element.shouldBeInstanceOf<LongArrayTag>()
                element.asLongArray shouldBe array
            }
        }
        checkAll(Arb.set(Arb.long(), 0..0x1000)) {
            val element = Nbt.encodeToNbtElement(it)
            element.shouldBeInstanceOf<LongArrayTag>()
            element.asLongArray shouldBe it.toLongArray()
        }
    }

    "enums should encode to strings" {
        checkAll(Exhaustive.enum<TestEnum>()) {
            val element = Nbt.encodeToNbtElement(it)
            element.shouldBeInstanceOf<StringTag>()
            element.asString().get() shouldBe it.name
        }
    }

    "closed polymorphism should encode correctly" {
        val value = SealedChild1(1f, 2.5)
        val element = Nbt.encodeToNbtElement<SealedBase>(value)
        element.shouldBeInstanceOf<CompoundTag>()
        element.getString("type").get() shouldBe "child1"
        with(element.getCompound("value").get()) {
            getFloat("baseVal").get() shouldBe value.baseVal
            getDouble("childProp").get() shouldBe value.childProp
        }
    }

    "defaults should only be encoded when the config value is set" {
        val value = TestClassWithDefault()
        with(Nbt.encodeToNbtElement(value)) {
            shouldBeInstanceOf<CompoundTag>()
            isEmpty shouldBe true
        }

        with(Nbt { encodeDefaults = true }.encodeToNbtElement(value)) {
            shouldBeInstanceOf<CompoundTag>()
            size() shouldBe 2
            getInt("one").get() shouldBe value.one
            getBoolean("tru").get() shouldBe value.tru
        }
    }
})
