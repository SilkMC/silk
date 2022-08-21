package net.silkmc.silk.core.mixin.server;

import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.silkmc.silk.core.event.player.PlayerEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public class MixinPlayerList {

    @Inject(method = "placeNewPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;<init>(Lnet/minecraft/server/MinecraftServer;Lnet/minecraft/network/Connection;Lnet/minecraft/server/level/ServerPlayer;)V", shift = At.Shift.AFTER))
    private void onPreLogin(Connection connection, ServerPlayer player, CallbackInfo ci) {
        PlayerEvents.INSTANCE.getPreLogin().invoke(new PlayerEvents.ServerPlayerEvent(player));
    }

    @Inject(method = "placeNewPlayer", at = @At("TAIL"))
    private void onPostLogin(Connection connection, ServerPlayer player, CallbackInfo ci) {
        PlayerEvents.INSTANCE.getPostLogin().invoke(new PlayerEvents.ServerPlayerEvent(player));
    }
}
