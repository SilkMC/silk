package net.silkmc.silk.core.mixin.player;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.silkmc.silk.core.event.player.PlayerItemEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public class MixinServerPlayer {

    @Inject(method = "drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;", at = @At("HEAD"))
    public void onDrop(ItemStack itemStack, boolean bl, boolean bl2, CallbackInfoReturnable<ItemEntity> cir) {
        var player = (ServerPlayer) (Object) this;
        var event = PlayerItemEvents.INSTANCE.getPreItemDrop().invoke(new PlayerItemEvents.ServerPlayerItemEvent(itemStack, player));
        if (event.isCancelled().get()) cir.cancel();
    }

}
