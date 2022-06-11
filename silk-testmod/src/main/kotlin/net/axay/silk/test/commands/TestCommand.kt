package net.axay.silk.test.commands

import net.axay.silk.commands.LiteralCommandBuilder
import net.axay.silk.test.Manager
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
