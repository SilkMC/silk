package net.silkmc.silk.core.mixin.level;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.silkmc.silk.core.Silk;
import net.silkmc.silk.core.event.BlockEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Level.class)
public abstract class MixinLevel {

    @Inject(
            method = "setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;II)Z",
            at = @At(
                    "HEAD"
            ),
            cancellable = true
    )
    private void prePlaceBlock(BlockPos pos, BlockState state, int flags, int maxUpdateDepth, CallbackInfoReturnable<Boolean> cir) {
        BlockEvents.BlockEvent blockEvent = new BlockEvents.BlockEvent((Level) (Object) this, pos, state);
        if (state.getBlock() == Blocks.AIR) {
            if (BlockEvents.INSTANCE.getPreDestroy().invoke(blockEvent).isCancelled().get()) {
                cir.setReturnValue(false);
            }
        } else {
            if (BlockEvents.INSTANCE.getPrePlace().invoke(blockEvent).isCancelled().get()) {
                cir.setReturnValue(false);
                for (ServerPlayer player : Silk.INSTANCE.getCurrentServer().getPlayerList().getPlayers()) {
                    player.containerMenu.sendAllDataToRemote(); // send inventory update to every player as we can't get the specific one from here
                }
            }
        }
    }
}
