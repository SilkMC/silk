package net.silkmc.silk.persistence.mixin.other;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.level.saveddata.SavedData;
import net.silkmc.silk.persistence.internal.CompoundSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

@Mixin(SavedData.class)
public class MixinSavedData {

    @SuppressWarnings("UnreachableCode")
    @Inject(
        method = "save(Ljava/io/File;Lnet/minecraft/core/HolderLookup$Provider;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/nbt/NbtUtils;addCurrentDataVersion(Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/nbt/CompoundTag;",
            shift = At.Shift.BEFORE
        ),
        cancellable = true
    )
    private void onSave(File file, HolderLookup.Provider provider,
                        CallbackInfo ci) {
        if (((SavedData) (Object) this) instanceof CompoundSavedData compoundSavedData) {
            if (compoundSavedData.getCompound().isEmpty() && !file.exists()) {
                ci.cancel();
            }
        }
    }
}
