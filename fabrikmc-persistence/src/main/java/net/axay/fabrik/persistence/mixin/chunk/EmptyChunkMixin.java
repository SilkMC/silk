package net.axay.fabrik.persistence.mixin.chunk;

import net.axay.fabrik.persistence.CompoundProvider;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.chunk.EmptyChunk;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(EmptyChunk.class)
public class EmptyChunkMixin implements CompoundProvider {
    @Unique
    private NbtCompound nbtCompound = new NbtCompound();

    @NotNull
    @Override
    public NbtCompound getCompound() {
        return nbtCompound;
    }

    @Override
    public void setCompound(@NotNull NbtCompound compound) {
        // do not set anything
    }
}
