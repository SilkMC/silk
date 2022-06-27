package net.silkmc.silk.core.kotlin

import net.minecraft.util.RandomSource
import net.minecraft.world.level.levelgen.BitRandomSource
import net.minecraft.world.level.levelgen.PositionalRandomFactory
import kotlin.random.Random
import kotlin.random.asJavaRandom

internal class WrappedMinecraftRandom(private val impl: RandomSource) : Random() {
    override fun nextBits(bitCount: Int): Int {
        return impl.nextInt().takeUpperBits(bitCount)
    }

    private fun Int.takeUpperBits(bitCount: Int): Int =
        this.ushr(32 - bitCount) and (-bitCount).shr(31)
}

internal class WrappedKotlinRandom(private var impl: Random) : BitRandomSource {
    override fun next(bits: Int): Int {
        return impl.nextBits(bits)
    }

    override fun fork(): RandomSource {
        return Random(impl.nextLong()).asMinecraftRandom()
    }

    override fun setSeed(seed: Long) {
        impl = Random(seed)
    }

    override fun nextGaussian(): Double {
        return impl.asJavaRandom().nextGaussian()
    }

    override fun forkPositional(): PositionalRandomFactory {
        error("forkPositional is not supported for a wrapped Kotlin random")
    }
}

/**
 * Returns a Kotlin [Random] wrapping this [RandomSource]. This gives you
 * access to the much more polished Kotlin random API.
 */
fun RandomSource.asKotlinRandom(): Random = WrappedMinecraftRandom(this)

/**
 * Returns a Minecraft [RandomSource] wrapped this Kotlin [Random].
 *
 * This should only be used in very simple cases, for example when calling
 * `Registry::getRandom`.
 */
fun Random.asMinecraftRandom(): RandomSource = WrappedKotlinRandom(this)
