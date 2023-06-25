package net.silkmc.silk.core.mixin.entity;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.silkmc.silk.core.event.EntityEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class MixinLivingEntity {

    @Inject(
        method = "hurt",
        at = @At(
            "HEAD"
        ),
        cancellable = true
    )
    private void onHurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        final var event = new EntityEvents.EntityDamageEvent(source, amount, (LivingEntity) (Object) this);
        if (EntityEvents.INSTANCE.getPreDamage().invoke(event).isCancelled().get()) {
            cir.setReturnValue(false);
        }
    }
}
