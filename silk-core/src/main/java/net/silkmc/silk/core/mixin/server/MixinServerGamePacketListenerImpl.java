package net.silkmc.silk.core.mixin.server;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.silkmc.silk.core.event.PlayerConnectionEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ServerGamePacketListenerImpl.class)
public class MixinServerGamePacketListenerImpl {

    @Shadow public ServerPlayer player;

    @ModifyArg(method = "onDisconnect", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;broadcastSystemMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/resources/ResourceKey;)V"))
    private Component changeQuitMessage(Component message) {
        Component component;
        PlayerConnectionEvents.PlayerQuitEvent event = new PlayerConnectionEvents.PlayerQuitEvent(player, message);
        PlayerConnectionEvents.INSTANCE.getPlayerQuit().invoke(event);
        component = event.getQuitMessage();
        return component;
    }
}
