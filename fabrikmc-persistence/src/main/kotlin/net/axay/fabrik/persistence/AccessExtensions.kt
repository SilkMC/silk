package net.axay.fabrik.persistence

import net.minecraft.world.chunk.Chunk

/**
 * Returns a persistent [net.minecraft.nbt.NbtCompound].
 *
 * @see CompoundProvider
 */
val Chunk.persistentCompound get() = (this as CompoundProvider).compound
