package net.axay.fabrik.persistence

import net.minecraft.entity.Entity
import net.minecraft.world.chunk.Chunk

/**
 * Returns a persistent [PersistentCompound].
 *
 * @see CompoundProvider.compound
 * @see CompoundProvider
 */
val Chunk.persistentCompound get() = (this as CompoundProvider).compound

/**
 * Returns a persistent [PersistentCompound].
 *
 * @see CompoundProvider.compound
 * @see CompoundProvider
 */
val Entity.persistentCompound get() = (this as CompoundProvider).compound
