package net.axay.fabrik.test.commands

import net.axay.fabrik.commands.LiteralCommandBuilder
import net.axay.fabrik.test.Manager
import net.minecraft.server.command.ServerCommandSource

fun testCommand(
    name: String,
    builder: LiteralCommandBuilder<ServerCommandSource>.() -> Unit,
) {
    Manager.testmodCommandBuilders[name] = builder
}
