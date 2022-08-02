package net.silkmc.silk.commands.mixin.client;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MessageSigner;
import net.silkmc.silk.commands.internal.ClientCommandHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public class MixinLocalPlayer {

    @Inject(method = "sendCommand", at = @At("HEAD"), cancellable = true)
    private void onCommand(String string, Component component, CallbackInfo ci) {
        //noinspection ConstantConditions
        if (ClientCommandHandler.INSTANCE.executeCommand(string, (LocalPlayer) (Object) this)) {
            ci.cancel();
        }
    }
}
