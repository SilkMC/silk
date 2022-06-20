package net.silkmc.silk.persistence

import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.chunk.ChunkAccess

/**
 * Returns a persistent [PersistentCompound].
 *
 * @see CompoundProvider.compound
 * @see CompoundProvider
 */
val ChunkAccess.persistentCompound get() = (this as CompoundProvider).compound

/**
 * Returns a persistent [PersistentCompound].
 *
 * @see CompoundProvider.compound
 * @see CompoundProvider
 */
val Entity.persistentCompound get() = (this as CompoundProvider).compound

/**
 * Returns a persistent [PersistentCompound].
 *
 * @see CompoundProvider.compound
 * @see CompoundProvider
 */
val ServerLevel.persistentCompound get() = (this as CompoundProvider).compound
