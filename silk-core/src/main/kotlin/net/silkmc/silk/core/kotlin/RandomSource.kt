package net.silkmc.silk.core.kotlin

import net.minecraft.util.RandomSource
import kotlin.random.Random

internal class WrappedMinecraftRandom(private val impl: RandomSource) : Random() {
    override fun nextBits(bitCount: Int): Int {
        return impl.nextInt().takeUpperBits(bitCount)
    }

    private fun Int.takeUpperBits(bitCount: Int): Int =
        this.ushr(32 - bitCount) and (-bitCount).shr(31)
}

/**
 * Returns a Kotlin [Random] wrappings this [RandomSource]. This gives you
 * access to the much more polished Kotlin random API.
 */
fun RandomSource.asKotlinRandom(): Random = WrappedMinecraftRandom(this)
