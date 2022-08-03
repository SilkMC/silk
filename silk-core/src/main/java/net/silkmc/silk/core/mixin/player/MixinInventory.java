package net.silkmc.silk.core.mixin.player;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.silkmc.silk.core.event.player.PlayerItemEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Inventory.class)
public class MixinInventory {

    @Inject(method = "add(Lnet/minecraft/world/item/ItemStack;)Z", at = @At(value = "HEAD"), cancellable = true)
    public void onPreCollectItem(ItemStack stack, CallbackInfoReturnable<Boolean> cir){
        var player = (Player) (Object) this;
        var event = PlayerItemEvents.INSTANCE.getItemCollect().invoke(new PlayerItemEvents.PlayerItemEvent(stack, player.level, player));
        if (event.isCancelled().get()) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

}
