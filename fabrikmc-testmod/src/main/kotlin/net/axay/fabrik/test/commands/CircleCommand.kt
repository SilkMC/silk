package net.axay.fabrik.test.commands

import net.axay.fabrik.commands.LiteralCommandBuilder
import net.axay.fabrik.core.math.geometry.produceCirclePositions
import net.axay.fabrik.core.math.geometry.produceFilledCirclePositions
import net.axay.fabrik.core.math.geometry.produceFilledSpherePositions
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.arguments.blocks.BlockInput
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.block.state.BlockState

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
