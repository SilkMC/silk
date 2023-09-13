package net.silkmc.silk.network.mixin.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.silkmc.silk.network.packet.ClientToServerPacketDefinition;
import net.silkmc.silk.network.packet.internal.SilkPacketPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerboundCustomPayloadPacket.class)
public class MixinServerboundCustomPayloadPacket {

    @Inject(
        method = "readPayload",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void onReadPayload(ResourceLocation resourceLocation,
                                      FriendlyByteBuf friendlyByteBuf,
                                      CallbackInfoReturnable<CustomPacketPayload> cir) {

        if (ClientToServerPacketDefinition.Companion.isRegistered(resourceLocation)) {
            cir.setReturnValue(new SilkPacketPayload(resourceLocation, friendlyByteBuf.readByteArray()));
        }
    }
}
