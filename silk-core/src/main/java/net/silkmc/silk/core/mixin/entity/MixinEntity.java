package net.silkmc.silk.core.mixin.entity;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.silkmc.silk.core.event.EntityEvents;
import net.silkmc.silk.core.event.EventScopeProperty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class MixinEntity {

    @Inject(
        method = "isInvulnerableToBase",
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
}
