package net.axay.silk.persistence.mixin.chunk;

import net.axay.silk.persistence.CompoundProvider;
import net.axay.silk.persistence.PersistentCompound;
import net.minecraft.world.level.chunk.ImposterProtoChunk;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ImposterProtoChunk.class)
public class ImposterProtoChunkMixin implements CompoundProvider {
    @Shadow @Final private LevelChunk wrapped;

    @NotNull
    @Override
    public PersistentCompound getCompound() {
        return ((CompoundProvider) wrapped).getCompound();
    }
}
