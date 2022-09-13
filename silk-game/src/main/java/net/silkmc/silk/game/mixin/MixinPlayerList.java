package net.silkmc.silk.game.mixin;

import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.silkmc.silk.game.tablist.Tablist;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public class MixinPlayerList {

    @Inject(method = "placeNewPlayer", at = @At("TAIL"))
    public void onJoin(Connection connection, ServerPlayer player, CallbackInfo ci) {
        Tablist tablist = Tablist.getCurrentTablist();
        if (tablist == null) return;
        tablist.addPlayer(player);
    }
}
