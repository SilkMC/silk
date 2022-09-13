package net.silkmc.silk.game.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.silkmc.silk.game.tablist.Tablist;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public class MixinServerGamePacketListenerImpl {

    @Shadow public ServerPlayer player;

    @Inject(method = "onDisconnect", at = @At("TAIL"))
    public void onQuit(Component reason, CallbackInfo ci) {
        Tablist tablist = Tablist.getCurrentTablist();
        if (tablist == null) return;
        tablist.removePlayer(player);
    }
}
