package net.silkmc.silk.network.mixin.server;

import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.silkmc.silk.network.packet.ClientToClientPacketDefinition;
import net.silkmc.silk.network.packet.ClientToServerPacketDefinition;
import net.silkmc.silk.network.packet.ServerPacketContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ServerGamePacketListenerImpl.class)
public class MixinServerGamePacketListenerImpl {

    @Inject(
        method = "handleCustomPayload",
        at = @At("HEAD"),
        cancellable = true
    )
    private void onHandleCustomPayload(ServerboundCustomPayloadPacket packet,
                                       CallbackInfo ci) {
        final var data = packet.getData();
        final var id = packet.getIdentifier();

        @SuppressWarnings("DataFlowIssue")
        final var context = new ServerPacketContext((ServerGamePacketListenerImpl) (Object) this);

        if (
            ClientToServerPacketDefinition.Companion.onReceive(id, data, context) ||
                ClientToClientPacketDefinition.Companion.onReceiveServer(id, data, context)
        ) {
            ci.cancel();
        }
    }
}
