package net.silkmc.silk.core.test

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import net.minecraft.world.level.levelgen.XoroshiroRandomSource
import net.silkmc.silk.core.kotlin.asKotlinRandom
import net.silkmc.silk.core.kotlin.asMinecraftRandom
import kotlin.random.Random
import kotlin.random.asJavaRandom

class RandomTest : FunSpec({
    context("test wrapper random stability") {
        val seed = 1234

        val kotlinRandom = Random(seed)
        val kotlinInts = (1..20).map { kotlinRandom.nextInt() }

        fun testRandom(ints: List<Int>, nextInt: () -> Int) {
            repeat(20) {
                ints[it] shouldBe nextInt()
            }
        }

        test("kotlin random the same as kotlin random") {
            val random = Random(seed)
            testRandom(kotlinInts) { random.nextInt() }
        }

        test("kotlin random as java random the same as kotlin random") {
            val random = Random(seed).asJavaRandom()
            testRandom(kotlinInts) { random.nextInt() }
        }

        test("kotlin random as minecraft random the same as kotlin random") {
            val random = Random(seed).asMinecraftRandom()
            testRandom(kotlinInts) { random.nextInt() }
        }

        val minecraftRandom = XoroshiroRandomSource(seed.toLong())
        val minecraftInts = (1..20).map { minecraftRandom.nextInt() }

        test("minecraft random the same as minecraft random") {
            val random = XoroshiroRandomSource(seed.toLong())
            testRandom(minecraftInts) { random.nextInt() }
        }

        test("minecraft random as kotlin random the same as minecraft random") {
            val random = XoroshiroRandomSource(seed.toLong()).asKotlinRandom()
            testRandom(minecraftInts) { random.nextInt() }
        }
    }
})
