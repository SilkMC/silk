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

import java.util.function.Consumer;

@Mixin(WorldChunk.class)
public class WorldChunkMixin implements CompoundProvider {
    @Unique
    private PersistentCompound compound = new PersistentCompoundImpl();

    @Inject(
            method = "<init>(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/chunk/ProtoChunk;Ljava/util/function/Consumer;)V",
            at = @At("RETURN")
    )
    private void initFromProtoChunk(ServerWorld serverWorld,
                                    ProtoChunk protoChunk,
                                    Consumer<WorldChunk> consumer,
                                    CallbackInfo ci) {
        compound = ((CompoundProvider) protoChunk).getCompound();
    }

    @NotNull
    @Override
    public PersistentCompound getCompound() {
        return compound;
    }
}
