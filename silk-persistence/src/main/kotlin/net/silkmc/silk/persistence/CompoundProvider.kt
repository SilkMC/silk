package net.silkmc.silk.persistence

/**
 * This class provides a [PersistentCompound]. Providers are classes like
 * [net.minecraft.world.level.chunk.ChunkAccess], [net.minecraft.world.entity.Entity] and more.
 *
 * @see compound
 */
interface CompoundProvider {
    /**
     * Returns a [PersistentCompound].
     *
     * Everything you write to this compound will be saved to disk, and can
     * be accessed at any time later.
     *
     * Even though this compound is persistent, accessing data from it and
     * writing data to it is fast, as this happens in memory.
     */
    var compound: PersistentCompound
}
