package net.silkmc.silk.persistence

import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.chunk.ChunkAccess
import net.silkmc.silk.core.annotations.InternalSilkApi

/**
 * Returns a persistent [PersistentCompound].
 *
 * @see CompoundProvider.compound
 * @see CompoundProvider
 */
val ChunkAccess.persistentCompound get() = compoundOrFallback()

/**
 * Returns a persistent [PersistentCompound].
 *
 * @see CompoundProvider.compound
 * @see CompoundProvider
 */
val Entity.persistentCompound get() = compoundOrFallback()

/**
 * Returns a persistent [PersistentCompound].
 *
 * @see CompoundProvider.compound
 * @see CompoundProvider
 */
val ServerLevel.persistentCompound get() = compoundOrFallback()

@InternalSilkApi
object PersistentCompoundFallback {
    var provider: ((Any) -> PersistentCompound)? = null
}

private fun Any.compoundOrFallback() = (this as? CompoundProvider)?.compound
    ?: PersistentCompoundFallback.provider?.invoke(this)
    ?: error("${this::class} has no persistent compound")
