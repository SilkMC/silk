package net.silkmc.silk.core.mixin.player;

import net.minecraft.advancements.Advancement;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.silkmc.silk.core.event.player.ServerPlayerEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerAdvancements.class)
public class MixinPlayerAdvancements {

    @Shadow
    private ServerPlayer player;

    @Redirect(method = "award", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;broadcastSystemMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/resources/ResourceKey;)V"))
    public void onAdvancement(PlayerList instance, Component message, ResourceKey<ChatType> messageType, Advancement advancement) {
        var event = new ServerPlayerEvents.AdvancementEvent(player, advancement, message);
        var cancellable = ServerPlayerEvents.INSTANCE.getOnAdvancement().invoke(event);
        if (cancellable.isCancelled().get()) {}
        else player.server.getPlayerList().broadcastSystemMessage(event.getMessage(), ChatType.SYSTEM);
    }

}
