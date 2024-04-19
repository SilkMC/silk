package net.silkmc.silk.network.mixin.client;

import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.silkmc.silk.network.packet.ClientPacketContext;
import net.silkmc.silk.network.packet.ServerToClientPacketDefinition;
import net.silkmc.silk.network.packet.internal.SilkPacketPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ClientCommonPacketListenerImpl.class)
public class MixinClientCommonPacketListenerImpl {

    @Inject(
        method = "handleCustomPayload(Lnet/minecraft/network/protocol/common/ClientboundCustomPayloadPacket;)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/util/thread/BlockableEventLoop;)V",
            shift = At.Shift.AFTER
        ),
        cancellable = true
    )
    private void onHandleCustomPayload(ClientboundCustomPayloadPacket packet,
                                       CallbackInfo ci) {

        //noinspection ConstantValue
        if (
            (Object) this instanceof ClientPacketListener &&
            packet.payload() instanceof SilkPacketPayload payload
        ) {
            final var context = new ClientPacketContext();

            if (ServerToClientPacketDefinition.Companion.onReceive(payload.type().id(), payload.getBytes(), context)) {
                ci.cancel();
            }
        }
    }
}
