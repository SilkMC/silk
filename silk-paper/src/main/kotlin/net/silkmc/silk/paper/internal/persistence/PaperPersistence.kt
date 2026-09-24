package net.silkmc.silk.paper.internal.persistence

import net.minecraft.nbt.CompoundTag
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.chunk.ChunkAccess
import net.silkmc.silk.persistence.PersistentCompoundFallback
import net.silkmc.silk.persistence.internal.ExternalPersistentCompound
import org.bukkit.craftbukkit.persistence.CraftPersistentDataContainer

private const val KEY = "silkmc:persistent_data"

/**
 * Replaces the persistence mixins: Silk data is stored in Paper's
 * PersistentDataContainer, which Paper saves together with its holder.
 */
fun setupPaperPersistence() {
    PersistentCompoundFallback.provider = { holder ->
        val pdc = when (holder) {
            is Entity -> holder.bukkitEntity.persistentDataContainer
            is ChunkAccess -> holder.persistentDataContainer
            is ServerLevel -> holder.world.persistentDataContainer
            else -> error("Unsupported persistent compound holder ${holder::class}")
        } as CraftPersistentDataContainer

        ExternalPersistentCompound({ pdc.getTag(KEY) as? CompoundTag }, { pdc.put(KEY, it) })
    }
}
