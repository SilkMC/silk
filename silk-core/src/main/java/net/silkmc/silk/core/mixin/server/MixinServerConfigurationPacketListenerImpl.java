package net.silkmc.silk.core.mixin.server;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.DisconnectionDetails;
import net.minecraft.server.network.ServerConfigurationPacketListenerImpl;
import net.silkmc.silk.core.event.PlayerEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerConfigurationPacketListenerImpl.class)
public abstract class MixinServerConfigurationPacketListenerImpl {

    @Shadow protected abstract GameProfile playerProfile();

    @Inject(
        method = "onDisconnect",
        at = @At("HEAD")
    )
    private void onQuitDuringConfiguration(DisconnectionDetails disconnectionDetails,
                                           CallbackInfo ci) {
        PlayerEvents.INSTANCE.getQuitDuringConfiguration()
            .invoke(new PlayerEvents.PlayerQuitDuringLoginEvent(playerProfile(), disconnectionDetails.reason()));
    }
}
