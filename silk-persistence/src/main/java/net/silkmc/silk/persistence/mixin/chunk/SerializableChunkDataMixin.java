package net.silkmc.silk.persistence.mixin.chunk;

import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.chunk.storage.RegionStorageInfo;
import net.minecraft.world.level.chunk.storage.SerializableChunkData;
import net.silkmc.silk.persistence.CompoundProvider;
import net.silkmc.silk.persistence.PersistentCompound;
import net.silkmc.silk.persistence.PersistentCompoundImpl;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SerializableChunkData.class)
public class SerializableChunkDataMixin implements CompoundProvider {

    @Unique
    private final PersistentCompound compound = new PersistentCompoundImpl();

    @Inject(method = "write", at = @At("RETURN"))
    private void onSerialize(CallbackInfoReturnable<CompoundTag> cir) {
        compound.putInCompound(cir.getReturnValue());
    }

    @Inject(method = "parse", at = @At("RETURN"))
    private static void onDeserialize(LevelHeightAccessor levelHeightAccessor,
                                      RegistryAccess registryAccess,
                                      CompoundTag nbt,
                                      CallbackInfoReturnable<SerializableChunkData> cir) {
        ((CompoundProvider) (Object) cir.getReturnValue()).getCompound()
                .loadFromCompound(nbt);
    }

    @Inject(method = "copyOf", at = @At("RETURN"))
    private static void loadCompoundFromChunk(ServerLevel serverLevel,
                                              ChunkAccess chunkAccess,
                                              CallbackInfoReturnable<SerializableChunkData> cir) {
        if (chunkAccess instanceof CompoundProvider chunkComponentProvider) {
            PersistentCompound chunkPersistentCompound = chunkComponentProvider.getCompound();
            CompoundTag serializableData = ((CompoundProvider) (Object) cir.getReturnValue()).getCompound().getData();
            if (serializableData == null) {
                return;
            }
            chunkPersistentCompound.putInCompound(serializableData, true);
        }
    }
    
    @Inject(method = "read", at = @At("RETURN"))
    private void setCompoundToChunk(ServerLevel serverLevel,
                                    PoiManager poiManager,
                                    RegionStorageInfo regionStorageInfo,
                                    ChunkPos chunkPos,
                                    CallbackInfoReturnable<ProtoChunk> cir) {
        CompoundTag compoundData = this.compound.getData();
        if (compoundData == null) return;
        ((CompoundProvider) cir.getReturnValue()).getCompound()
                .loadFromCompound(compoundData, true);
    }

    @Override
    public @NotNull PersistentCompound getCompound() {
        return compound;
    }
}
