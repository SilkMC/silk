package net.silkmc.silk.core.mixin.server;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.DisconnectionDetails;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import net.silkmc.silk.core.event.PlayerEvents;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLoginPacketListenerImpl.class)
public abstract class MixinServerLoginPacketListenerImpl {

    @Shadow private @Nullable GameProfile authenticatedProfile;

    @Inject(
        method = "onDisconnect",
        at = @At("HEAD")
    )
    private void onQuitDuringLogin(DisconnectionDetails disconnectionDetails,
                                   CallbackInfo ci) {
        PlayerEvents.INSTANCE.getQuitDuringLogin()
            .invoke(new PlayerEvents.PlayerQuitDuringLoginEvent(authenticatedProfile, disconnectionDetails.reason()));
    }
}
