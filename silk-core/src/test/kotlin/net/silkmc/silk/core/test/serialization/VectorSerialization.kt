package net.silkmc.silk.core.test.serialization

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import kotlinx.serialization.encodeToString
import net.silkmc.silk.core.math.vector.Vec3i
import net.silkmc.silk.core.serialization.SilkJson

class VectorSerialization : FunSpec({
    val json = SilkJson

    context("serialize 3d vectors") {
        test("blockpos") {
            json.encodeToString(Vec3i(1, 2, 3)) shouldBe """{"x":1,"y":2,"z":3}"""
        }
    }
})
