package net.axay.fabrik.nbt

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class NbtDecodingTest : StringSpec({
    "compound should decode to class" {
        val value = TestClass(
            10,
            -1248,
            "Taito",
            listOf("abc", "äèðßèäßð", "1 2"),
            setOf(5, 34, 9, 3443),
            InnerTestClass(true)
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
        }

        Nbt.decodeFromNbtElement<TestClass>(compound) shouldBe value
    }
})
