package net.silkmc.silk.network.mixin.server;

import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.silkmc.silk.network.packet.ClientToClientPacketDefinition;
import net.silkmc.silk.network.packet.ClientToServerPacketDefinition;
import net.silkmc.silk.network.packet.ServerPacketContext;
import net.silkmc.silk.network.packet.internal.SilkPacketPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ServerCommonPacketListenerImpl.class)
public class MixinServerCommonPacketListenerImpl {

    @Inject(
        method = "handleCustomPayload",
        at = @At("HEAD"),
        cancellable = true
    )
    private void onHandleCustomPayload(ServerboundCustomPayloadPacket packet,
                                       CallbackInfo ci) {

        //noinspection ConstantValue
        if (
            (Object) this instanceof ServerGamePacketListenerImpl listener &&
            packet.payload() instanceof SilkPacketPayload payload
        ) {
            final var context = new ServerPacketContext(listener);

            if (
                ClientToServerPacketDefinition.Companion.onReceive(payload.id(), payload.getBytes(), context) ||
                    ClientToClientPacketDefinition.Companion.onReceiveServer(payload.id(), payload.getBytes(), context)
            ) {
                ci.cancel();
            }
        }
    }
}
