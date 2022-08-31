package net.silkmc.silk.persistence.mixin.chunk;

import net.minecraft.world.level.chunk.EmptyLevelChunk;
import net.silkmc.silk.persistence.CompoundProvider;
import net.silkmc.silk.persistence.EmptyPersistentCompound;
import net.silkmc.silk.persistence.PersistentCompound;
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
