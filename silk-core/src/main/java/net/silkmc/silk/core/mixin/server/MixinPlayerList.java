package net.silkmc.silk.core.mixin.server;

import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.silkmc.silk.core.event.PlayerConnectionEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public class MixinPlayerList {

    @Unique private ServerPlayer silk$temporaryPlayer = null;
    
    @Inject(method = "placeNewPlayer", at = @At("HEAD"))
    private void onPlayerJoin(Connection connection, ServerPlayer player, CallbackInfo ci) {
        silk$temporaryPlayer = player;
    }

    @ModifyArg(
            method = "placeNewPlayer",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/players/PlayerList;broadcastSystemMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/resources/ResourceKey;)V"
            )
    )
    private Component changeJoinMessage(Component message) {
        Component component;
        PlayerConnectionEvents.PlayerJoinEvent event = new PlayerConnectionEvents.PlayerJoinEvent(silk$temporaryPlayer, message);
        PlayerConnectionEvents.INSTANCE.getPlayerJoin().invoke(event);
        component = event.getJoinMessage();
        silk$temporaryPlayer = null;
        return component;
    }
}
