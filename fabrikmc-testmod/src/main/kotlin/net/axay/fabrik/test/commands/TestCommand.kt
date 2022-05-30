package net.axay.fabrik.test.commands

import net.axay.fabrik.commands.LiteralCommandBuilder
import net.axay.fabrik.test.Manager
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
