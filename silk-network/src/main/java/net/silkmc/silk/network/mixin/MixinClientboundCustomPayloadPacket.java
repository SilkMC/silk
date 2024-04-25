package net.silkmc.silk.network.mixin;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.silkmc.silk.network.packet.ServerToClientPacketDefinition;
import net.silkmc.silk.network.packet.internal.SilkStreamCodecFallbackProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ClientboundCustomPayloadPacket.class)
public class MixinClientboundCustomPayloadPacket {

    @ModifyArg(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload;codec(Lnet/minecraft/network/protocol/common/custom/CustomPacketPayload$FallbackProvider;Ljava/util/List;)Lnet/minecraft/network/codec/StreamCodec;"
        ),
        index = 0
    )
    private static CustomPacketPayload.FallbackProvider<FriendlyByteBuf> injectFallbackCodec(
        CustomPacketPayload.FallbackProvider<FriendlyByteBuf> standardFallbackProvider) {

        return new SilkStreamCodecFallbackProvider(
            ServerToClientPacketDefinition.Companion,
            standardFallbackProvider,
            "server-to-client");
    }
}
