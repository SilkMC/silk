package net.silkmc.silk.core.mixin.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.silkmc.silk.core.event.BlockEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
class MixinBlock {

  @Inject(
      method = "playerWillDestroy",
      at = @At("HEAD")
  )
  private void onBreak(Level level, BlockPos blockPos, BlockState blockState, Player player, CallbackInfoReturnable<BlockState> cir) {
    BlockEvents.INSTANCE.getBlockBreak().invoke(new BlockEvents.BlockEvent<>(player, blockState.getBlock()));
  }

  @Inject(
      method = "setPlacedBy",
      at = @At("HEAD")
  )
  private void onPlaced(Level level, BlockPos blockPos, BlockState blockState, LivingEntity livingEntity, ItemStack itemStack, CallbackInfo ci) {
    BlockEvents.INSTANCE.getBlockPlace().invoke(new BlockEvents.BlockEvent<>(livingEntity, blockState.getBlock()));
  }
}