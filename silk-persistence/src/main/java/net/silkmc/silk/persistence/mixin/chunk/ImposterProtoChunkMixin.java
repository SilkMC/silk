package net.silkmc.silk.persistence.mixin.chunk;

import net.minecraft.world.level.chunk.ImposterProtoChunk;
import net.minecraft.world.level.chunk.LevelChunk;
import net.silkmc.silk.persistence.CompoundProvider;
import net.silkmc.silk.persistence.PersistentCompound;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ImposterProtoChunk.class)
public class ImposterProtoChunkMixin implements CompoundProvider {

    @Shadow @Final private LevelChunk wrapped;

    @Unique
    @NotNull
    @Override
    public PersistentCompound getCompound() {
        return ((CompoundProvider) wrapped).getCompound();
    }
}
