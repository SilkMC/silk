package net.silkmc.silk.core.mixin.level;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.silkmc.silk.core.event.EntityEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerLevel.class)
public class MixinServerLevel {

  @Inject(
      method = "addEntity",
      at = @At(
          value = "INVOKE",
          target = "Lnet/minecraft/world/level/entity/PersistentEntitySectionManager;addNewEntity(Lnet/minecraft/world/level/entity/EntityAccess;)Z",
          shift = At.Shift.BEFORE
      ),
      cancellable = true
  )
  private void addEntity(Entity entity, CallbackInfoReturnable<Boolean> cir) {
    var event = new EntityEvents.EntitySpawnEvent(
        entity
    );

    EntityEvents.INSTANCE.getOnSpawn().invoke(event);

    if (event.isCancelled().get()) {
      cir.cancel();
    }
  }

}
