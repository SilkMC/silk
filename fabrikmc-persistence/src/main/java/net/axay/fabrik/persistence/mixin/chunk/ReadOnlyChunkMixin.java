package net.axay.fabrik.persistence.mixin.chunk;

import net.axay.fabrik.persistence.CompoundProvider;
import net.axay.fabrik.persistence.PersistentCompound;
import net.minecraft.world.chunk.ReadOnlyChunk;
import net.minecraft.world.chunk.WorldChunk;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ReadOnlyChunk.class)
public class ReadOnlyChunkMixin implements CompoundProvider {
    @Shadow @Final private WorldChunk wrapped;

    @NotNull
    @Override
    public PersistentCompound getCompound() {
        return ((CompoundProvider) wrapped).getCompound();
    }
}
