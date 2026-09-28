package net.silkmc.silk.core.mixin.level;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.UseOnContext;
import net.silkmc.silk.core.event.PlayerEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public class MixinBlockItem {

  @Inject(
      method = "useOn",
      at = @At("HEAD"),
      cancellable = true)
  private void useOn(UseOnContext useOnContext, CallbackInfoReturnable<InteractionResult> cir) {
    var player = useOnContext.getPlayer();

    if (player == null) return;

    var event = new PlayerEvents.PlayerInteractEvent(
        useOnContext.getPlayer(),
        useOnContext.getHand(),
        useOnContext.getLevel().getBlockState(useOnContext.getClickedPos()).getBlock(),
        null
    );

    PlayerEvents.INSTANCE.getOnInteract().invoke(event);

    if (event.isCancelled().get()) {
      cir.cancel();
    }
  }
}
