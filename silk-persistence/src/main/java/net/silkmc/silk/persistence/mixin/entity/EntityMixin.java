package net.silkmc.silk.persistence.mixin.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.silkmc.silk.persistence.CompoundProvider;
import net.silkmc.silk.persistence.PersistentCompound;
import net.silkmc.silk.persistence.PersistentCompoundImpl;
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

    @Inject(method = "saveWithoutId", at = @At("RETURN"))
    private void onWriteNbt(CompoundTag nbt, CallbackInfoReturnable<CompoundTag> cir) {
        compound.putInCompound(cir.getReturnValue());
    }

    @Inject(
            method = "load",
            at = @At( // the entity is valid if the following function is called
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;readAdditionalSaveData(Lnet/minecraft/nbt/CompoundTag;)V",
                    shift = At.Shift.AFTER
            )
    )
    private void onReadNbt(CompoundTag nbt, CallbackInfo ci) {
        compound.loadFromCompound(nbt);
    }

    @NotNull
    @Override
    public PersistentCompound getCompound() {
        return compound;
    }
}
