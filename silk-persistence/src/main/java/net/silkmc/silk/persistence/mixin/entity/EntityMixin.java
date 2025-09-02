package net.silkmc.silk.persistence.mixin.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.silkmc.silk.persistence.CompoundProvider;
import net.silkmc.silk.persistence.PersistentCompound;
import net.silkmc.silk.persistence.PersistentCompoundImpl;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(Entity.class)
public class EntityMixin implements CompoundProvider {

    @Unique
    private final PersistentCompound compound = new PersistentCompoundImpl();

    @Inject(method = "saveWithoutId", at = @At("RETURN"))
    private void onWriteNbt(ValueOutput valueOutput, CallbackInfo ci) {
        if (compound.isEmpty()) {
            return;
        }

        CompoundTag tag = new CompoundTag();
        compound.putInCompound(tag, true);
        valueOutput.store(PersistentCompoundImpl.CUSTOM_DATA_KEY, CompoundTag.CODEC, tag);
    }

    @Inject(
            method = "load",
            at = @At( // the entity is valid if the following function is called
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/Entity;readAdditionalSaveData(Lnet/minecraft/world/level/storage/ValueInput;)V",
                    shift = At.Shift.AFTER
            )
    )
    private void onReadNbt(ValueInput valueInput, CallbackInfo ci) {
        Optional<CompoundTag> data = valueInput.read(PersistentCompoundImpl.CUSTOM_DATA_KEY, CompoundTag.CODEC);
        compound.loadFromCompound(data.orElseGet(CompoundTag::new), true);
    }

    @Unique
    @NotNull
    @Override
    public PersistentCompound getCompound() {
        return compound;
    }
}
