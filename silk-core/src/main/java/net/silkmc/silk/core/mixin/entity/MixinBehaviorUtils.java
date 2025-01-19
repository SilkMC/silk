package net.silkmc.silk.core.mixin.entity;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.silkmc.silk.core.event.EntityEvents;
import net.silkmc.silk.core.event.EventScopeProperty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BehaviorUtils.class)
public class MixinBehaviorUtils {

    @Inject(
        method = "throwItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/phys/Vec3;F)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z",
            shift = At.Shift.BEFORE
        ),
        cancellable = true
    )
    private static void throwItem(LivingEntity livingEntity, ItemStack itemStack, Vec3 vec3, Vec3 vec32, float f, CallbackInfo ci, @Local ItemEntity itemEntity) {
        final var event = new EntityEvents.EntityDropItemEvent(
            livingEntity,
            itemEntity,
            new EventScopeProperty<>(false)
        );

        EntityEvents.INSTANCE.getDropItem().invoke(event);

        if (event.isCancelled().get()) {
            ci.cancel();
        }
    }
}
