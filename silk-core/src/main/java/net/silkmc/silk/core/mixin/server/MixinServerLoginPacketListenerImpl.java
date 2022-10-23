package net.silkmc.silk.core.mixin.server;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import net.silkmc.silk.core.event.PlayerEvents;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLoginPacketListenerImpl.class)
public class MixinServerLoginPacketListenerImpl {

    @Shadow @Nullable private ServerPlayer delayedAcceptPlayer;

    @Inject(
        method = "onDisconnect",
        at = @At("HEAD")
    )
    private void onPreQuit(Component reason, CallbackInfo ci) {
        PlayerEvents.INSTANCE.getQuitDuringLogin().invoke(new PlayerEvents.PlayerEvent<>(delayedAcceptPlayer));
    }
}
