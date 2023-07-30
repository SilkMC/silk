package net.silkmc.silk.network.mixin.client;

import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.silkmc.silk.network.packet.ClientPacketContext;
import net.silkmc.silk.network.packet.ClientToClientPacketDefinition;
import net.silkmc.silk.network.packet.ServerToClientPacketDefinition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = ClientPacketListener.class)
public class MixinClientPacketListener {

    @Inject(
        method = "handleCustomPayload",
        at = @At("HEAD"),
        cancellable = true
    )
    private void onHandleCustomPayload(ClientboundCustomPayloadPacket packet,
                                       CallbackInfo ci) {
        final var data = packet.getData();
        final var id = packet.getIdentifier();

        @SuppressWarnings("DataFlowIssue")
        final var context = new ClientPacketContext((ClientPacketListener) (Object) this);

        if (ServerToClientPacketDefinition.Companion.onReceive(id, data, context)) {
            ci.cancel();
        }
    }
}
