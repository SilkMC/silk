package net.axay.fabrik.persistence.mixin.chunk;

import net.axay.fabrik.persistence.CompoundProvider;
import net.axay.fabrik.persistence.EmptyPersistentCompound;
import net.axay.fabrik.persistence.PersistentCompound;
import net.minecraft.world.chunk.EmptyChunk;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EmptyChunk.class)
public class EmptyChunkMixin implements CompoundProvider {
    @NotNull
    @Override
    public PersistentCompound getCompound() {
        return EmptyPersistentCompound.INSTANCE;
    }
}
