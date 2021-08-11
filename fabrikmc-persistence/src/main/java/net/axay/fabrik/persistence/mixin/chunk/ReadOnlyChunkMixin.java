package net.axay.fabrik.persistence.mixin.chunk;

import net.axay.fabrik.persistence.CompoundProvider;
import net.minecraft.nbt.NbtCompound;
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
    public NbtCompound getCompound() {
        return ((CompoundProvider) wrapped).getCompound();
    }

    @Override
    public void setCompound(@NotNull NbtCompound compound) {
        ((CompoundProvider) wrapped).setCompound(compound);
    }
}
