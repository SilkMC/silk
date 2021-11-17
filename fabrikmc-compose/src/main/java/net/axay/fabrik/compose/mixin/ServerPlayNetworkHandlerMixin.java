package net.axay.fabrik.compose.mixin;

import net.axay.fabrik.compose.MinecraftComposeGui;
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
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
    private void onHandSwingInject(HandSwingC2SPacket packet, CallbackInfo ci) {
        MinecraftComposeGui.Companion.onSwingHand$fabrikmc_compose(player, packet);
    }

    @Inject(
        method = "onUpdateSelectedSlot",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/network/ServerPlayerEntity;getInventory()Lnet/minecraft/entity/player/PlayerInventory;",
            ordinal = 1,
            shift = At.Shift.BEFORE
        ),
        cancellable = true
    )
    private void onUpdateSelectedSlotInject(UpdateSelectedSlotC2SPacket packet, CallbackInfo ci) {
        if (MinecraftComposeGui.Companion.onUpdateSelectedSlot$fabrikmc_compose(player, packet)) {
            ci.cancel();
        }
    }
}
