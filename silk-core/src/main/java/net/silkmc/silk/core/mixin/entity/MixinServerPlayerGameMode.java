package net.silkmc.silk.core.mixin.entity;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.silkmc.silk.core.event.EventScopeProperty;
import net.silkmc.silk.core.event.PlayerEvents;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerGameMode.class)
public class MixinServerPlayerGameMode {

    @Shadow
    @Final
    protected ServerPlayer player;

    @Inject(
        method = "destroyBlock",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Block;playerWillDestroy(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/entity/player/Player;)Lnet/minecraft/world/level/block/state/BlockState;",
            shift = At.Shift.BEFORE
        ),
        cancellable = true
    )
    private void onBlockDestroy(BlockPos blockPos, CallbackInfoReturnable<Boolean> cir, @Local BlockEntity entity, @Local BlockState state) {
        var event = new PlayerEvents.PlayerBlockBreakEvent(player, blockPos, state, entity, new EventScopeProperty<>(false));

        PlayerEvents.INSTANCE.getBlockBreak().invoke(event);

        if (event.isCancelled().get()) {
            cir.setReturnValue(false);
        }
    }
}
