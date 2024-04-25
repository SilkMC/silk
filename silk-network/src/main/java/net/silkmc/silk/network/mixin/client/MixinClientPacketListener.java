package net.silkmc.silk.network.mixin.client;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.silkmc.silk.network.packet.ClientPacketContext;
import net.silkmc.silk.network.packet.internal.SilkPacketPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class MixinClientPacketListener {

    @Inject(
        method = "handleCustomPayload",
        at = @At("HEAD"),
        cancellable = true
    )
    private void onHandleCustomPayload(CustomPacketPayload customPacketPayload,
                                       CallbackInfo ci) {
        if (customPacketPayload instanceof SilkPacketPayload payload) {
            final var context = new ClientPacketContext();
            if (payload.onReceiveClient(context)) {
                ci.cancel();
            }
        }
    }
}
