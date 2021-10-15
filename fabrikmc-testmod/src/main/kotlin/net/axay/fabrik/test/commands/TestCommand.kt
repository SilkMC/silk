package net.axay.fabrik.test.commands

import net.axay.fabrik.commands.LiteralCommandBuilder
import net.axay.fabrik.test.Manager
import net.fabricmc.fabric.api.client.command.v1.FabricClientCommandSource
import net.minecraft.server.command.ServerCommandSource

fun testCommand(
    name: String,
    builder: LiteralCommandBuilder<ServerCommandSource>.() -> Unit,
) {
    Manager.testmodCommandBuilders[name] = builder
}

fun clientTestCommand(
    name: String,
    builder: LiteralCommandBuilder<FabricClientCommandSource>.() -> Unit,
) {
    Manager.clientTestmodCommandBuilders[name] = builder
}
