package net.silkmc.silk.core.mixin.server;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.silkmc.silk.core.event.PlayerEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public class MixinServerGamePacketListenerImpl {

    @Shadow public ServerPlayer player;

    @Inject(
        method = "onDisconnect",
        at = @At("HEAD")
    )
    private void onPreQuit(Component reason, CallbackInfo ci) {
        PlayerEvents.INSTANCE.getPreQuit().invoke(new PlayerEvents.PlayerEvent<>(player));
    }
}
