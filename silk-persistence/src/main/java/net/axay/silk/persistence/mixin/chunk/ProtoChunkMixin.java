package net.axay.silk.persistence.mixin.chunk;

import net.axay.silk.persistence.CompoundProvider;
import net.axay.silk.persistence.PersistentCompound;
import net.axay.silk.persistence.PersistentCompoundImpl;
import net.minecraft.world.level.chunk.ProtoChunk;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ProtoChunk.class)
public class ProtoChunkMixin implements CompoundProvider {
    @Unique
    private final PersistentCompound compound = new PersistentCompoundImpl();

    @NotNull
    @Override
    public PersistentCompound getCompound() {
        return compound;
    }
}
