package net.axay.fabrik.igui.mixin;

import net.minecraft.screen.ScreenHandlerSyncHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerPlayerEntity.class)
public interface ServerPlayerEntityAccessor {
    @Accessor("screenHandlerSyncHandler")
    ScreenHandlerSyncHandler getScreenHandlerSyncHandler();
}
