package net.axay.fabrik.persistence.mixin.chunk;

import net.axay.fabrik.persistence.CompoundProvider;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.chunk.EmptyChunk;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EmptyChunk.class)
public class EmptyChunkMixin implements CompoundProvider {
    @NotNull
    @Override
    public NbtCompound getCompound() {
        return new NbtCompound();
    }

    @Override
    public void setCompound(@NotNull NbtCompound compound) {
        // do not set anything
    }
}
