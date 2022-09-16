package net.silkmc.silk.game.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.silkmc.silk.game.tablist.Tablist;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public class MixinServerPlayer {

    ServerPlayer player = (ServerPlayer) (Object) this;

    @Inject(method = "getTabListDisplayName", at = @At("RETURN"), cancellable = true)
    public void onGetTablistName(CallbackInfoReturnable<Component> cir) {
        Tablist tablist = Tablist.getCurrentTablist();
        if (tablist == null) return;
        var generatedName = tablist.getPlayerNames().get(player.getUUID());
        if (generatedName == null) return;
        cir.setReturnValue(generatedName.component1());
        cir.cancel();
    }
}