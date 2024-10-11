package net.silkmc.silk.persistence.mixin.chunk;

import net.minecraft.world.level.chunk.EmptyLevelChunk;
import net.silkmc.silk.persistence.CompoundProvider;
import net.silkmc.silk.persistence.EmptyPersistentCompound;
import net.silkmc.silk.persistence.PersistentCompound;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EmptyLevelChunk.class)
public class EmptyLevelChunkMixin implements CompoundProvider {

    @Unique
    @NotNull
    @Override
    public PersistentCompound getCompound() {
        return EmptyPersistentCompound.INSTANCE;
    }

    @Override
    public void setCompound(@NotNull PersistentCompound persistentCompound) {

    }
}
