package net.silkmc.silk.core.mixin.player;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.silkmc.silk.core.event.EventScopeProperty;
import net.silkmc.silk.core.event.PlayerEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class MixinServerPlayer {

  @Inject(
      method = "die",
      at = @At(
          value = "INVOKE",
          target = "Lnet/minecraft/server/network/ServerGamePacketListenerImpl;send(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketSendListener;)V",
          shift = At.Shift.BEFORE
      )
  )
  private void die(DamageSource damageSource, CallbackInfo ci, @Local LocalRef<Component> component) {
    var event = new PlayerEvents.PlayerDeathEvent(
        (ServerPlayer) (Object) this,
        damageSource,
        new EventScopeProperty<>(component.get()));

    PlayerEvents.INSTANCE.getOnDeath().invoke(event);

    component.set(event.getDeathMessage().get());
  }

}
