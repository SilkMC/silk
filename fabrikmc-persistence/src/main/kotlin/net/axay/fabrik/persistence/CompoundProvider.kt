package net.axay.fabrik.persistence

import net.minecraft.nbt.NbtCompound

/**
 * This class provides an [NbtCompound] which is persistent.
 *
 * Everything you write to this compound will be saved to disk, and can
 * be accessed at any time later.
 */
interface CompoundProvider {
    var compound: NbtCompound
}
