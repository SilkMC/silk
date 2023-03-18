package net.silkmc.silk.commands.mixin.client;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.protocol.game.ClientboundCommandsPacket;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import net.minecraft.world.flag.FeatureFlags;
import net.silkmc.silk.commands.internal.ClientCommandHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class MixinClientPacketListener {

    @Shadow
    private CommandDispatcher<SharedSuggestionProvider> commands;

    @Shadow @Final private Minecraft minecraft;

    @Inject(
        method = "handleLogin",
        at = @At("RETURN")
    )
    private void onHandleLogin(ClientboundLoginPacket packet, CallbackInfo ci) {
        final var context = CommandBuildContext.simple(packet.registryHolder(), FeatureFlags.DEFAULT_FLAGS);
        ClientCommandHandler.INSTANCE.refreshDispatcher(context);
    }

    @Inject(
        method = "handleCommands",
        at = @At("RETURN")
    )
    private void onHandleCommands(ClientboundCommandsPacket packet, CallbackInfo ci) {
        ClientCommandHandler.INSTANCE.applyCommands(commands);
    }

    @Inject(
        method = "sendCommand",
        at = @At("HEAD"),
        cancellable = true
    )
    private void onSendCommand(String command, CallbackInfo ci) {
        final var player = minecraft.player;
        if (player != null) {
            if (ClientCommandHandler.INSTANCE.executeCommand(command, player)) {
                ci.cancel();
            }
        }
    }
}
