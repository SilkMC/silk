package net.silkmc.silk.network.mixin;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.silkmc.silk.network.packet.ClientToServerPacketDefinition;
import net.silkmc.silk.network.packet.internal.SilkPacketPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Function;

@Mixin(ServerboundCustomPayloadPacket.class)
public class MixinServerboundCustomPayloadPacket {
    @Redirect(
            method = "<clinit>",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/network/codec/StreamCodec;map(Ljava/util/function/Function;Ljava/util/function/Function;)Lnet/minecraft/network/codec/StreamCodec;")
    )
    private static StreamCodec<FriendlyByteBuf, ServerboundCustomPayloadPacket> changePacketCodec(StreamCodec<FriendlyByteBuf, CustomPacketPayload> payloadCodec, Function<? super CustomPacketPayload, ? extends ServerboundCustomPayloadPacket> function, Function<? super ServerboundCustomPayloadPacket, ? extends CustomPacketPayload> function2) {
        return new StreamCodec<>() {
            @Override
            public void encode(FriendlyByteBuf buf, ServerboundCustomPayloadPacket packet) {
                if (packet.payload() instanceof SilkPacketPayload silkPayload) {
                    buf.writeResourceLocation(silkPayload.type().id());
                    buf.writeByteArray(silkPayload.getBytes());
                    return;
                }

                payloadCodec.encode(buf, packet.payload());
            }

            @Override
            public ServerboundCustomPayloadPacket decode(FriendlyByteBuf buf) {
                // copying buf, so we can provide it untouched to the original codec if payload's id is not registered
                FriendlyByteBuf bufCopy = new FriendlyByteBuf(buf.copy());
                ResourceLocation id = buf.readResourceLocation();
                if (ClientToServerPacketDefinition.Companion.isRegistered(id)) {
                    return new ServerboundCustomPayloadPacket(new SilkPacketPayload(id, buf.readByteArray()));
                }

                // clearing the original buf, so player won't be kicked because of unread bytes
                buf.clear();
                return new ServerboundCustomPayloadPacket(payloadCodec.decode(bufCopy));
            }
        };
    }
}
