package net.axay.fabrik.persistence

import net.minecraft.world.chunk.Chunk

val Chunk.persistentCompound get() = (this as CompoundProvider).compound
