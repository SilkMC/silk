package net.silkmc.silk.core.mixin.server;

import net.minecraft.server.MinecraftServer;
import net.silkmc.silk.core.event.ServerEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
        //noinspection ConstantConditions
        ServerEvents.INSTANCE.getPreStart().invoke(new ServerEvents.ServerEvent((MinecraftServer) (Object) this));
    }

    @Inject(
        method = "runServer",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/MinecraftServer;initServer()Z",
            shift = At.Shift.BEFORE
        )
    )
    private void onStarted(CallbackInfo ci) {
        //noinspection ConstantConditions
        ServerEvents.INSTANCE.getStarted().invoke(new ServerEvents.ServerEvent((MinecraftServer) (Object) this));
    }

    @Inject(
        method = "stopServer",
        at = @At("HEAD")
    )
    private void onBeforeStop(CallbackInfo ci) {
        //noinspection ConstantConditions
        ServerEvents.INSTANCE.getPreStop().invoke(new ServerEvents.ServerEvent((MinecraftServer) (Object) this));
    }

    @Inject(
        method = "stopServer",
        at = @At("TAIL")
    )
    private void onStopped(CallbackInfo ci) {
        //noinspection ConstantConditions
        ServerEvents.INSTANCE.getStopped().invoke(new ServerEvents.ServerEvent((MinecraftServer) (Object) this));
    }
}
