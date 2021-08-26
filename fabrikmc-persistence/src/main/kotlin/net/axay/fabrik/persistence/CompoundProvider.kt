package net.axay.fabrik.persistence

/**
 * This class provides a [PersistentCompound]. Providers are classes like
 * [net.minecraft.world.chunk.Chunk], [net.minecraft.entity.Entity] and more.
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
    val compound: PersistentCompound
}
