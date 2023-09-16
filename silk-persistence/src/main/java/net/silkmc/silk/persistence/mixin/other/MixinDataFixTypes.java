package net.silkmc.silk.persistence.mixin.other;

import com.mojang.datafixers.DataFixer;
import com.mojang.serialization.Dynamic;
import net.minecraft.util.datafix.DataFixTypes;
import net.silkmc.silk.persistence.internal.CompoundPersistentState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DataFixTypes.class)
public class MixinDataFixTypes {

    @Inject(
        method = "update(Lcom/mojang/datafixers/DataFixer;Lcom/mojang/serialization/Dynamic;II)Lcom/mojang/serialization/Dynamic;",
        at = @At("HEAD"),
        cancellable = true
    )
    private <T> void onUpdate(DataFixer dataFixer,
                              Dynamic<T> input,
                              int oldVersion, int newVersion,
                              CallbackInfoReturnable<Dynamic<T>> cir) {
        // has this been called during a persistent data read operation?
        if (CompoundPersistentState.Companion.getBLOCK_DATA_FIXER().get()) {
            // just return the input
            cir.setReturnValue(input);
        }
    }
}
