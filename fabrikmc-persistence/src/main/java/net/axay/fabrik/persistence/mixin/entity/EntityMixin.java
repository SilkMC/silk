package net.axay.fabrik.persistence.mixin.entity;

import net.axay.fabrik.persistence.CompoundProvider;
import net.axay.fabrik.persistence.PersistentCompound;
import net.axay.fabrik.persistence.PersistentCompoundImpl;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin implements CompoundProvider {
    @Unique
    private final PersistentCompound compound = new PersistentCompoundImpl();

    @Inject(method = "writeNbt", at = @At("RETURN"))
    private void onWriteNbt(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> cir) {
        compound.putInCompound$fabrikmc_persistence(cir.getReturnValue());
    }

    @Inject(
            method = "readNbt",
            at = @At( // the entity is valid if the following function is called
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/Entity;readCustomDataFromNbt(Lnet/minecraft/nbt/NbtCompound;)V",
                    shift = At.Shift.AFTER
            )
    )
    private void onReadNbt(NbtCompound nbt, CallbackInfo ci) {
        compound.loadFromCompound$fabrikmc_persistence(nbt);
    }

    @NotNull
    @Override
    public PersistentCompound getCompound() {
        return compound;
    }
}
