package net.silkmc.silk.core.mixin.server;

import net.minecraft.server.MinecraftServer;
import net.silkmc.silk.core.internal.events.ServerEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer {

    @Inject(
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/MinecraftServer;initServer()Z",
            shift = At.Shift.AFTER
        ),
        method = "runServer"
    )
    private void onStarting(CallbackInfo ci) {
        //noinspection ConstantConditions
        ServerEvents.Init.Companion.invoke((MinecraftServer) (Object) this);
    }
}
