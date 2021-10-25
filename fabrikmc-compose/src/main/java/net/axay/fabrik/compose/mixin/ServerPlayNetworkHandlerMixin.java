package net.axay.fabrik.compose.mixin;

import net.axay.fabrik.compose.MinecraftComposeGui;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class ServerPlayNetworkHandlerMixin {
    @Shadow public ServerPlayerEntity player;

    @Inject(method = "onHandSwing", at = @At("RETURN"))
    private void onLeftClick(HandSwingC2SPacket packet, CallbackInfo ci) {
        MinecraftComposeGui.Companion.onLeftClick((player), packet);
    }
}
