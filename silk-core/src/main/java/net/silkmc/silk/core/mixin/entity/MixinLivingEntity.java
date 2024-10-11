package net.silkmc.silk.core.mixin.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.silkmc.silk.core.event.EntityEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class MixinLivingEntity {

    @Inject(
        method = "actuallyHurt",
        at = @At("HEAD")
    )
    private void onActuallyHurt(ServerLevel serverLevel,
                                DamageSource damageSource,
                                float amount,
                                CallbackInfo ci) {
        EntityEvents.INSTANCE.getDamageLivingEntity()
            .invoke(new EntityEvents.EntityDamageEvent((LivingEntity) (Object) this, amount, damageSource));
    }
}
