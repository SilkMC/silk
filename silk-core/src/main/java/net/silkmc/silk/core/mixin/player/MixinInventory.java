package net.silkmc.silk.core.mixin.player;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.silkmc.silk.core.event.player.PlayerItemEvents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Inventory.class)
public class MixinInventory {

    @Shadow
    @Final
    public Player player;

    @Inject(method = "add(Lnet/minecraft/world/item/ItemStack;)Z", at = @At(value = "HEAD"), cancellable = true)
    public void onPreCollectItem(ItemStack itemStack, CallbackInfoReturnable<Boolean> cir) {
        var event = PlayerItemEvents.INSTANCE.getPreItemCollect().invoke(new PlayerItemEvents.ServerPlayerItemEvent(itemStack, (ServerPlayer) player));
        if (event.isCancelled().get()) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

}
