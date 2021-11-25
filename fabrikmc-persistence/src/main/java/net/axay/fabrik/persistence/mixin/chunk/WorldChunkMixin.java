package net.axay.fabrik.persistence.mixin.chunk;

import net.axay.fabrik.persistence.CompoundProvider;
import net.axay.fabrik.persistence.PersistentCompound;
import net.axay.fabrik.persistence.PersistentCompoundImpl;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.chunk.ProtoChunk;
import net.minecraft.world.chunk.WorldChunk;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldChunk.class)
public class WorldChunkMixin implements CompoundProvider {
    @Unique
    private PersistentCompound compound = new PersistentCompoundImpl();

    // this targets the constructor which creates a WorldChunk from a ProtoChunk
    @Inject(
            method = "<init>(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/chunk/ProtoChunk;Lnet/minecraft/world/chunk/WorldChunk$EntityLoader;)V",
            at = @At("RETURN")
    )
    private void initFromProtoChunk(ServerWorld world,
                                    ProtoChunk protoChunk,
                                    WorldChunk.EntityLoader entityLoader,
                                    CallbackInfo ci) {
        compound = ((CompoundProvider) protoChunk).getCompound();
    }

    @NotNull
    @Override
    public PersistentCompound getCompound() {
        return compound;
    }
}
