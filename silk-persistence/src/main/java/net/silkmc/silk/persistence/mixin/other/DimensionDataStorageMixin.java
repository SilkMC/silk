package net.silkmc.silk.persistence.mixin.other;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.saveddata.SavedDataType;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.silkmc.silk.persistence.PersistentCompoundImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.nio.file.Files;
import java.nio.file.Path;

@Mixin(DimensionDataStorage.class)
public class DimensionDataStorageMixin {

    @WrapOperation(
        method = "tryWrite",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/nbt/NbtIo;writeCompressed(Lnet/minecraft/nbt/CompoundTag;Ljava/nio/file/Path;)V"
        )
    )
    private void dontSaveEmptyCompounds(CompoundTag compoundTag, Path path, Operation<Void> original,
                                        @Local(argsOnly = true, ordinal = 0) SavedDataType<?> savedDataType) {
        // don't create a file if compound is empty
        if (Files.notExists(path)
                && savedDataType.id().equals(PersistentCompoundImpl.CUSTOM_DATA_KEY)
                && compoundTag.getCompoundOrEmpty("data").isEmpty()) {
            return;
        }

        original.call(compoundTag, path);
    }
}
