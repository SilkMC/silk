package net.silkmc.silk.core.mixin.server;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.PlayerList;
import net.silkmc.silk.core.event.EventScopeProperty;
import net.silkmc.silk.core.event.PlayerEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public class MixinPlayerList {

  @Inject(
      method = "placeNewPlayer",
      at = @At(
          value = "INVOKE",
          target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;<init>(Lnet/minecraft/server/MinecraftServer;Lnet/minecraft/network/Connection;Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/server/network/CommonListenerCookie;)V",
          shift = At.Shift.BEFORE
      )
  )
  private void onPreLogin(Connection connection, ServerPlayer player, CommonListenerCookie commonListenerCookie, CallbackInfo ci) {
    PlayerEvents.INSTANCE.getPreLogin().invoke(new PlayerEvents.PlayerEvent<>(player));
  }

  @Inject(
      method = "placeNewPlayer",
      at = @At(
          value = "INVOKE",
          target = "Lnet/minecraft/server/players/PlayerList;broadcastSystemMessage(Lnet/minecraft/network/chat/Component;Z)V",
          shift = At.Shift.BEFORE)
  )
  private void onPostLogin(Connection connection, ServerPlayer player, CommonListenerCookie commonListenerCookie, CallbackInfo ci, @Local LocalRef<MutableComponent> mutableComponent) {
    var event = new PlayerEvents.PostLoginEvent(player, new EventScopeProperty<>(mutableComponent.get()));
    PlayerEvents.INSTANCE.getPostLogin().invoke(event);
    mutableComponent.set(event.getJoinMessage().get().copy());
  }
}
