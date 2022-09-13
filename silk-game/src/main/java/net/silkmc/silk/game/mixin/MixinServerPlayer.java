package net.silkmc.silk.game.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.silkmc.silk.game.tablist.Tablist;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayer.class)
public class MixinServerPlayer {

    ServerPlayer player = (ServerPlayer) (Object) this;

    @Redirect(method = "getTabListDisplayName", at = @At(value = "HEAD"))
    public Component onGetTablistName() {
        Tablist tablist = Tablist.getCurrentTablist();
        if (tablist == null) return null;
        return tablist.getPlayerNames().get(player.getUUID());
    }

}