package net.silkmc.silk.compose.mixin;

import net.silkmc.silk.compose.MinecraftComposeGui;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {

    @Shadow public ServerPlayer player;

    @Inject(method = "handleAnimate", at = @At("RETURN"))
    private void onHandSwingInject(ServerboundSwingPacket packet, CallbackInfo ci) {
        MinecraftComposeGui.Companion.onSwingHand$silk_compose(player, packet);
    }

    @Inject(
        method = "handleSetCarriedItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerPlayer;getInventory()Lnet/minecraft/world/entity/player/Inventory;",
            ordinal = 1,
            shift = At.Shift.BEFORE
        ),
        cancellable = true
    )
    private void onUpdateSelectedSlotInject(ServerboundSetCarriedItemPacket packet, CallbackInfo ci) {
        if (MinecraftComposeGui.Companion.onUpdateSelectedSlot$silk_compose(player, packet)) {
            ci.cancel();
        }
    }
}
