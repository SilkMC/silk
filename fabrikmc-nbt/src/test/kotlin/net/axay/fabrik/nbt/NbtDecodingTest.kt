package net.axay.fabrik.nbt

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
import io.kotest.property.forAll
import net.minecraft.nbt.NbtList

class NbtDecodingTest : StringSpec({
    "compound should decode to class" {
        val value = TestClass(
            10,
            -1248,
            "Joe",
            listOf("abc", "äèðßèäßð", "1 2"),
            setOf(5, 34, 9, 3443),
            InnerTestClass(true),
            null
        )
        val compound = nbtCompound {
            put("x", value.x)
            put("y", value.y)
            put("name", value.name)
            list("stringList", value.stringList)
            longArray("longSet", value.longSet)
            compound("inner") {
                put("test", value.inner.test)
            }
            list("nullable", listOf<Short>())
        }
        Nbt.decodeFromNbtElement<TestClass>(compound) shouldBe value
    }

    "lists should decode to nullable types" {
        Nbt.decodeFromNbtElement<Int?>(NbtList()) shouldBe null
        Nbt.decodeFromNbtElement<Int?>(NbtList().apply { add(5.toNbt()) }) shouldBe 5
    }

    "byte array should decode to collections" {
        checkAll(Arb.byteArrays(Arb.int(0..0x1000), Arb.byte())) {
            val nbt = it.toNbt()
            val list = it.toList()
            Nbt.decodeFromNbtElement<ByteArray>(nbt) shouldBe it
            Nbt.decodeFromNbtElement<List<Byte>>(nbt) shouldBe list
        }
        checkAll(Arb.set(Arb.byte(), 0..Byte.MAX_VALUE)) {
            val nbt = it.toList().toNbt()
            Nbt.decodeFromNbtElement<Set<Byte>>(nbt) shouldBe it
        }
    }

    "int array should decode to collections" {
        checkAll(Arb.list(Arb.int(), 0..0x1000)) {
            val nbt = it.toNbt()
            val array = it.toIntArray()
            Nbt.decodeFromNbtElement<IntArray>(nbt) shouldBe array
            Nbt.decodeFromNbtElement<List<Int>>(nbt) shouldBe it
        }
        checkAll(Arb.set(Arb.int(), 0..0x1000)) {
            val nbt = it.toList().toNbt()
            Nbt.decodeFromNbtElement<Set<Int>>(nbt) shouldBe it
        }
    }

    "long array should decode to collections" {
        checkAll(Arb.list(Arb.long(), 0..0x1000)) {
            val nbt = it.toNbt()
            val array = it.toLongArray()
            Nbt.decodeFromNbtElement<LongArray>(nbt) shouldBe array
            Nbt.decodeFromNbtElement<List<Long>>(nbt) shouldBe it
        }
        checkAll(Arb.set(Arb.long(), 0..0x1000)) {
            val nbt = it.toList().toNbt()
            Nbt.decodeFromNbtElement<Set<Long>>(nbt) shouldBe it
        }
    }

    "strings should decode to enums" {
        forAll(Exhaustive.enum<TestEnum>()) {
            Nbt.decodeFromNbtElement<TestEnum>(it.name.toNbt()) == it
        }
    }

    "closed polymorphism should decode correctly" {
        val value = SealedChild1(1f, 2.5)
        val compound = nbtCompound {
            put("type", "child1")
            compound("value") {
                put("baseVal", value.baseVal)
                put("childProp", value.childProp)
            }
        }
        Nbt.decodeFromNbtElement<SealedBase>(compound) shouldBe value
    }
})
