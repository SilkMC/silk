package net.silkmc.silk.test.commands

import net.minecraft.commands.CommandSourceStack
import net.silkmc.silk.commands.ClientCommandSourceStack
import net.silkmc.silk.commands.LiteralCommandBuilder
import net.silkmc.silk.test.SilkTest

fun testCommand(
    name: String,
    builder: LiteralCommandBuilder<CommandSourceStack>.() -> Unit,
) {
    SilkTest.testmodCommandBuilders[name] = builder
}

fun clientTestCommand(
    name: String,
    builder: LiteralCommandBuilder<ClientCommandSourceStack>.() -> Unit,
) {
    SilkTest.clientTestmodCommandBuilders[name] = builder
}
