package net.axay.silk.persistence.mixin.chunk;

import net.axay.silk.persistence.CompoundProvider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.chunk.storage.ChunkSerializer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkSerializer.class)
public class ChunkSerializerMixin {
    @Inject(method = "write", at = @At("RETURN"))
    private static void onSerialize(ServerLevel world,
                                    ChunkAccess chunk,
                                    CallbackInfoReturnable<CompoundTag> cir) {
        ((CompoundProvider) chunk).getCompound()
                .putInCompound(cir.getReturnValue().getCompound("Level"));
    }

    @Inject(method = "read", at = @At("RETURN"))
    private static void onDeserialize(ServerLevel world,
                                      PoiManager poiStorage,
                                      ChunkPos pos,
                                      CompoundTag nbt,
                                      CallbackInfoReturnable<ProtoChunk> cir) {
        ((CompoundProvider) cir.getReturnValue()).getCompound()
                .loadFromCompound(nbt.getCompound("Level"));
    }
}
