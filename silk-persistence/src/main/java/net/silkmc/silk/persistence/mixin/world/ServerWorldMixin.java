package net.silkmc.silk.persistence.mixin.world;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.silkmc.silk.persistence.CompoundProvider;
import net.silkmc.silk.persistence.PersistentCompound;
import net.silkmc.silk.persistence.PersistentCompoundImpl;
import net.silkmc.silk.persistence.internal.CompoundPersistentState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public abstract class ServerWorldMixin implements CompoundProvider {

    @Unique
    private final PersistentCompound compound = new PersistentCompoundImpl();

    @Shadow public abstract DimensionDataStorage getDataStorage();

    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {
        try {
            CompoundPersistentState.Companion.getBLOCK_DATA_FIXER().set(true);
            getDataStorage().computeIfAbsent(
                new SavedData.Factory<SavedData>(
                    () -> new CompoundPersistentState(compound),
                    (nbt, provider) -> CompoundPersistentState.Companion.load(nbt, compound),
                    DataFixTypes.LEVEL // not actually, but we cannot use null here
                ),
                PersistentCompoundImpl.CUSTOM_DATA_KEY
            );
        } finally {
            CompoundPersistentState.Companion.getBLOCK_DATA_FIXER().set(false);
        }
    }

    @NotNull
    @Override
    public PersistentCompound getCompound() {
        return compound;
    }
}
