package net.axay.fabrik.test.commands

import net.axay.fabrik.commands.LiteralCommandBuilder
import net.axay.fabrik.commands.command
import net.axay.fabrik.core.math.geometry.produceCirclePositions
import net.axay.fabrik.core.math.geometry.produceFilledCirclePositions
import net.axay.fabrik.core.math.geometry.produceFilledSpherePositions
import net.minecraft.block.BlockState
import net.minecraft.command.argument.BlockStateArgument
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.server.network.ServerPlayerEntity

val circleCommand = command("circle") {
    literal("hollow") {
        buildCircleLikeLogic { radius, blockState ->
            blockPos.produceCirclePositions(radius) { world.setBlockState(it, blockState) }
        }
    }

    literal("filled") {
        buildCircleLikeLogic { radius, blockState ->
            blockPos.produceFilledCirclePositions(radius) { world.setBlockState(it, blockState) }
        }
    }
}

val sphereCommand = command("sphere") {
    literal("filled") {
        buildCircleLikeLogic { radius, blockState ->
            blockPos.produceFilledSpherePositions(radius) { world.setBlockState(it, blockState) }
        }
    }
}

inline fun LiteralCommandBuilder<ServerCommandSource>.buildCircleLikeLogic(
    crossinline logic: ServerPlayerEntity.(radius: Int, blockState: BlockState) -> Unit
) {
    argument<Int>("radius") { radiusArg ->
        argument<BlockStateArgument>("block") { blockStateArg ->
            runs {
                source.player.logic(radiusArg(), blockStateArg().blockState)
            }
        }
    }
}
