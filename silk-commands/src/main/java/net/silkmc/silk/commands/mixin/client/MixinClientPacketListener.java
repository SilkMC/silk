package net.silkmc.silk.commands.mixin.client;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.protocol.game.ClientboundCommandsPacket;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import net.silkmc.silk.commands.internal.ClientCommandHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class MixinClientPacketListener {

    @Shadow
    private CommandDispatcher<SharedSuggestionProvider> commands;

    @Inject(
        method = "handleLogin",
        at = @At("RETURN")
    )
    private void onLogin(ClientboundLoginPacket packet, CallbackInfo ci) {
        ClientCommandHandler.INSTANCE.refreshDispatcher(new CommandBuildContext(packet.registryHolder()));
    }

    @Inject(
        method = "handleCommands",
        at = @At("RETURN")
    )
    private void onCommand(ClientboundCommandsPacket packet, CallbackInfo ci) {
        ClientCommandHandler.INSTANCE.applyCommands(commands);
    }
}
