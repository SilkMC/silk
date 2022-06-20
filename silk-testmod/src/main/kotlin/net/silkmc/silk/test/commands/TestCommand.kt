package net.silkmc.silk.test.commands

import net.silkmc.silk.commands.LiteralCommandBuilder
import net.silkmc.silk.test.Manager
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import net.minecraft.commands.CommandSourceStack

fun testCommand(
    name: String,
    builder: LiteralCommandBuilder<CommandSourceStack>.() -> Unit,
) {
    Manager.testmodCommandBuilders[name] = builder
}

fun clientTestCommand(
    name: String,
    builder: LiteralCommandBuilder<FabricClientCommandSource>.() -> Unit,
) {
    Manager.clientTestmodCommandBuilders[name] = builder
}
