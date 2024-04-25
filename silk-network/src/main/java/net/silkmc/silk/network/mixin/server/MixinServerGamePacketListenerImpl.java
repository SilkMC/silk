package net.silkmc.silk.network.mixin.server;

import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.silkmc.silk.network.packet.ServerPacketContext;
import net.silkmc.silk.network.packet.internal.SilkPacketPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public class MixinServerGamePacketListenerImpl {

    @SuppressWarnings("UnreachableCode")
    @Inject(
        method = "handleCustomPayload",
        at = @At("HEAD"),
        cancellable = true
    )
    private void onHandleCustomPayload(ServerboundCustomPayloadPacket packet,
                                       CallbackInfo ci) {
        if (packet.payload() instanceof SilkPacketPayload payload) {
            final var context = new ServerPacketContext((ServerGamePacketListenerImpl) (Object) this);
            if (payload.onReceiveServer(context)) {
                ci.cancel();
            }
        }
    }
}
