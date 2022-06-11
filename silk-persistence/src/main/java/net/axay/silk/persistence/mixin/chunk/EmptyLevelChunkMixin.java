package net.axay.silk.persistence.mixin.chunk;

import net.axay.silk.persistence.CompoundProvider;
import net.axay.silk.persistence.EmptyPersistentCompound;
import net.axay.silk.persistence.PersistentCompound;
import net.minecraft.world.level.chunk.EmptyLevelChunk;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EmptyLevelChunk.class)
public class EmptyLevelChunkMixin implements CompoundProvider {
    @NotNull
    @Override
    public PersistentCompound getCompound() {
        return EmptyPersistentCompound.INSTANCE;
    }
}
