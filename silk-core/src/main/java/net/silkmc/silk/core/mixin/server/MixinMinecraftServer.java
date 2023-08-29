package net.silkmc.silk.core.mixin.server;

import net.minecraft.server.MinecraftServer;
import net.silkmc.silk.core.event.ServerEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("DataFlowIssue")
@Mixin(MinecraftServer.class)
public class MixinMinecraftServer {

    @Inject(
        method = "runServer",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/MinecraftServer;initServer()Z",
            shift = At.Shift.BEFORE
        )
    )
    private void onBeforeInit(CallbackInfo ci) {
        ServerEvents.INSTANCE.getPreStart().invoke(new ServerEvents.ServerEvent((MinecraftServer) (Object) this));
    }

    @Inject(
        method = "runServer",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/MinecraftServer;buildServerStatus()Lnet/minecraft/network/protocol/status/ServerStatus;",
            shift = At.Shift.AFTER
        )
    )
    private void onStarted(CallbackInfo ci) {
        ServerEvents.INSTANCE.getPostStart().invoke(new ServerEvents.ServerEvent((MinecraftServer) (Object) this));
    }

    @Inject(
        method = "stopServer",
        at = @At("HEAD")
    )
    private void onBeforeStop(CallbackInfo ci) {
        ServerEvents.INSTANCE.getPreStop().invoke(new ServerEvents.ServerEvent((MinecraftServer) (Object) this));
    }

    @Inject(
        method = "stopServer",
        at = @At("TAIL")
    )
    private void onStopped(CallbackInfo ci) {
        ServerEvents.INSTANCE.getPostStop().invoke(new ServerEvents.ServerEvent((MinecraftServer) (Object) this));
    }

    @Inject(
        method = "halt",
        at = @At("HEAD"),
        cancellable = true
    )
    private void onHalt(CallbackInfo ci) {
        final var event = new ServerEvents.PreHaltEvent((MinecraftServer) (Object) this);
        ServerEvents.INSTANCE.getPreHalt().invoke(event);
        if (event.isCancelled().get()) {
            ci.cancel();
        }
    }
}
