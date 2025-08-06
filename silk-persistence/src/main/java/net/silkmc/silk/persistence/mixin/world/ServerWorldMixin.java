package net.silkmc.silk.persistence.mixin.world;

import net.minecraft.server.level.ServerLevel;
import net.silkmc.silk.persistence.CompoundProvider;
import net.silkmc.silk.persistence.PersistentCompound;
import net.silkmc.silk.persistence.PersistentCompoundImpl;
import net.silkmc.silk.persistence.internal.CompoundSavedData;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.class)
public abstract class ServerWorldMixin implements CompoundProvider {

    @Unique
    private final PersistentCompound compound = new PersistentCompoundImpl();

    @SuppressWarnings("UnreachableCode")
    @Inject(method = "<init>", at = @At("RETURN"))
    private void onInit(CallbackInfo ci) {
        final var serverLevel = (ServerLevel) (Object) this;

        try {
            CompoundSavedData.Companion.getShouldBlockDataFixer$silk_persistence().set(true);

            final var dataStorage = serverLevel.getDataStorage();
            final var legacyDataType = CompoundSavedData.Companion.createType(PersistentCompoundImpl.LEGACY_CUSTOM_DATA_KEY, compound);
            final var dataType = CompoundSavedData.Companion.createType(PersistentCompoundImpl.CUSTOM_DATA_KEY, compound);

            // move legacy data
            final var legacyData = dataStorage.get(legacyDataType);
            if (legacyData != null) {
                dataStorage.set(dataType, legacyData);
                // note: we do not delete the legacy data, as there is no official way to do so
            }

            dataStorage.computeIfAbsent(dataType);
        } finally {
            CompoundSavedData.Companion.getShouldBlockDataFixer$silk_persistence().set(false);
        }
    }

    @Unique
    @NotNull
    @Override
    public PersistentCompound getCompound() {
        return compound;
    }
}
