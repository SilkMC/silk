package net.silkmc.silk.core.mixin.entity;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.silkmc.silk.core.event.EntityEvents;
import net.silkmc.silk.core.event.EventScopeProperty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class MixinEntity {

    @Inject(
        method = "isInvulnerableTo",
        at = @At("RETURN"),
        cancellable = true
    )
    private void onIsInvulnerableTo(DamageSource damageSource,
                                    CallbackInfoReturnable<Boolean> cir) {
        final var returnValue = cir.getReturnValue();

        final var event = new EntityEvents.EntityCheckInvulnerabilityEvent(
            (Entity) (Object) this,
            damageSource,
            new EventScopeProperty<>(returnValue)
        );

        EntityEvents.INSTANCE.getCheckInvulnerability().invoke(event);

        if (event.isInvulnerable().get() != returnValue) {
            cir.setReturnValue(event.isInvulnerable().get());
        }
    }

    @Inject(
        method = "spawnAtLocation(Lnet/minecraft/world/item/ItemStack;F)Lnet/minecraft/world/entity/item/ItemEntity;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z",
            shift = At.Shift.BEFORE
        ),
        cancellable = true
    )
    private void onSpawnAtLocation(ItemStack itemStack, float f, CallbackInfoReturnable<ItemEntity> cir, @Local ItemEntity itemEntity) {
        if (cir.getReturnValue() == null) return;

        final var event = new EntityEvents.EntityDropItemEvent(
            (Entity) (Object) this,
            itemEntity,
            new EventScopeProperty<>(false)
        );

        EntityEvents.INSTANCE.getDropItem().invoke(event);

        if (event.isCancelled().get()) {
            cir.setReturnValue(null);
        }
    }
}
