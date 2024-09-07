package net.silkmc.silk.core.mixin.entity;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.silkmc.silk.core.event.EntityEvents;
import net.silkmc.silk.core.event.EventScopeProperty;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity {
    @Shadow public abstract ItemStack eat(Level level, ItemStack itemStack);

    @Unique
    private Boolean isDroppingLoot = false;

    @Inject(
        method = "actuallyHurt",
        at = @At("HEAD")
    )
    private void onActuallyHurt(DamageSource damageSource,
                                float amount,
                                CallbackInfo ci) {
        EntityEvents.INSTANCE.getDamageLivingEntity()
            .invoke(new EntityEvents.EntityDamageEvent((LivingEntity) (Object) this, amount, damageSource));
    }

    @Inject(
        method = "createWitherRose",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z",
            shift = At.Shift.BEFORE
        ),
        cancellable = true
    )
    private void onCreateWitherRose(LivingEntity livingEntity, CallbackInfo ci, @Local ItemEntity itemEntity) {
        // Death event - start
        if (isDroppingLoot) {
            ci.cancel();
            return;
        }
        // Death event - end

        // Drop item event - start
        final var event = new EntityEvents.EntityDropItemEvent(
            livingEntity,
            itemEntity,
            new EventScopeProperty<>(false)
        );

        EntityEvents.INSTANCE.getDropItem().invoke(event);

        if (event.isCancelled().get()) {
            ci.cancel();
        }
        // Drop item event - end
    }

    @Inject(
        method = "die",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/damagesource/DamageSource;getEntity()Lnet/minecraft/world/entity/Entity;",
            shift = At.Shift.BEFORE
        ),
        cancellable = true
    )
    private void onDie(DamageSource source, CallbackInfo ci) {
        var entity = (LivingEntity) (Object) this;
        final var event = new EntityEvents.EntityDeathEvent(
            entity,
            source,
            new EventScopeProperty<>(true),
            new EventScopeProperty<>(false)
        );

        EntityEvents.INSTANCE.getDeath().invoke(event);

        if (event.isCancelled().get()) {
            entity.setHealth(1F);
            ci.cancel();
            return;
        }

        isDroppingLoot = event.isDroppingLoot().get();
    }

    @Inject(
        method = "dropAllDeathLoot",
        at = @At("HEAD"),
        cancellable = true
    )
    private void onDropAllDeathLoot(ServerLevel serverLevel, DamageSource damageSource, CallbackInfo ci) {
        // Death event - start
        if (isDroppingLoot) {
            ci.cancel();
        }
        // Death event - end
    }
}
