package net.silkmc.silk.network.mixin;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.silkmc.silk.network.packet.ServerToClientPacketDefinition;
import net.silkmc.silk.network.packet.internal.SilkPacketPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Function;

@Mixin(ClientboundCustomPayloadPacket.class)
public class MixinClientboundCustomPayloadPacket {
    @Redirect(
            method = "<clinit>",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/network/codec/StreamCodec;map(Ljava/util/function/Function;Ljava/util/function/Function;)Lnet/minecraft/network/codec/StreamCodec;", ordinal = 0)
    )
    private static StreamCodec<RegistryFriendlyByteBuf, ClientboundCustomPayloadPacket> changePacketCodec(StreamCodec<RegistryFriendlyByteBuf, CustomPacketPayload> payloadCodec, Function<? super CustomPacketPayload, ? extends ClientboundCustomPayloadPacket> function, Function<? super ClientboundCustomPayloadPacket, ? extends CustomPacketPayload> function2) {
        return new StreamCodec<>() {
            @Override
            public void encode(RegistryFriendlyByteBuf buf, ClientboundCustomPayloadPacket packet) {
                if (packet.payload() instanceof SilkPacketPayload silkPayload) {
                    buf.writeResourceLocation(silkPayload.type().id());
                    buf.writeByteArray(silkPayload.getBytes());
                    return;
                }

                payloadCodec.encode(buf, packet.payload());
            }

            @Override
            public ClientboundCustomPayloadPacket decode(RegistryFriendlyByteBuf buf) {
                // copying buf, so we can provide it untouched to the original codec if payload's id is not registered
                RegistryFriendlyByteBuf bufCopy = new RegistryFriendlyByteBuf(buf.copy(), buf.registryAccess());
                ResourceLocation id = buf.readResourceLocation();
                if (ServerToClientPacketDefinition.Companion.isRegistered(id)) {
                    return new ClientboundCustomPayloadPacket(new SilkPacketPayload(id, buf.readByteArray()));
                }

                // clearing the original buf, so player won't be kicked because of unread bytes
                buf.clear();
                return new ClientboundCustomPayloadPacket(payloadCodec.decode(bufCopy));
            }
        };
    }
}
