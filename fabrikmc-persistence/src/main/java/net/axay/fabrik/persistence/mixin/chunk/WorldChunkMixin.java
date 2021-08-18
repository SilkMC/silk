package net.axay.fabrik.persistence.mixin.chunk;

import net.axay.fabrik.persistence.CompoundProvider;
import net.minecraft.nbt.NbtCompound;
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
    private NbtCompound nbtCompound = new NbtCompound();

    @Inject(
            method = "<init>(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/world/chunk/ProtoChunk;Ljava/util/function/Consumer;)V",
            at = @At("RETURN")
    )
    private void initFromProtoChunk(ServerWorld serverWorld,
                                    ProtoChunk protoChunk,
                                    Consumer<WorldChunk> consumer,
                                    CallbackInfo ci) {
        nbtCompound = ((CompoundProvider) protoChunk).getCompound();
    }

    @NotNull
    @Override
    public NbtCompound getCompound() {
        return nbtCompound;
    }

    @Override
    public void setCompound(@NotNull NbtCompound compound) {
        nbtCompound = compound;
    }
}
