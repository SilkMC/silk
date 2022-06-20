package net.silkmc.silk.test.commands

import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.arguments.blocks.BlockInput
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.block.state.BlockState
import net.silkmc.silk.commands.LiteralCommandBuilder
import net.silkmc.silk.core.math.geometry.produceCirclePositions
import net.silkmc.silk.core.math.geometry.produceFilledCirclePositions
import net.silkmc.silk.core.math.geometry.produceFilledSpherePositions

val circleCommand = testCommand("circle") {
    literal("hollow") {
        buildCircleLikeLogic { radius, blockState ->
            blockPosition().produceCirclePositions(radius) { level.setBlockAndUpdate(it, blockState) }
        }
    }

    literal("filled") {
        buildCircleLikeLogic { radius, blockState ->
            blockPosition().produceFilledCirclePositions(radius) { level.setBlockAndUpdate(it, blockState) }
        }
    }
}

val sphereCommand = testCommand("sphere") {
    literal("filled") {
        buildCircleLikeLogic { radius, blockState ->
            blockPosition().produceFilledSpherePositions(radius) { level.setBlockAndUpdate(it, blockState) }
        }
    }
}

inline fun LiteralCommandBuilder<CommandSourceStack>.buildCircleLikeLogic(
    crossinline logic: ServerPlayer.(radius: Int, blockState: BlockState) -> Unit
) {
    argument<Int>("radius") { radiusArg ->
        argument<BlockInput>("block") { blockStateArg ->
            runs {
                source.playerOrException.logic(radiusArg(), blockStateArg().state)
            }
        }
    }
}
