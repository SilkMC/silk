package net.silkmc.silk.persistence.mixin.chunk;

import net.minecraft.world.level.chunk.ProtoChunk;
import net.silkmc.silk.persistence.CompoundProvider;
import net.silkmc.silk.persistence.PersistentCompound;
import net.silkmc.silk.persistence.PersistentCompoundImpl;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ProtoChunk.class)
public class ProtoChunkMixin implements CompoundProvider {

    @Unique
    private PersistentCompound compound = new PersistentCompoundImpl();

    @Unique
    @NotNull
    @Override
    public PersistentCompound getCompound() {
        return compound;
    }

    @Override
    public void setCompound(@NotNull PersistentCompound compound) {
        this.compound = compound;
    }
}
