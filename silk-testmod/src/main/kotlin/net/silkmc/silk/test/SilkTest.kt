package net.silkmc.silk.test

import net.minecraft.commands.CommandSourceStack
import net.minecraft.network.chat.Component
import net.silkmc.silk.commands.ClientCommandSourceStack
import net.silkmc.silk.commands.LiteralCommandBuilder
import net.silkmc.silk.commands.command
import net.silkmc.silk.test.commands.*
import net.silkmc.silk.test.events.ServerEventTest
import net.silkmc.silk.test.network.NetworkTest

object SilkTest {
    internal val testmodCommandBuilders = HashMap<String, LiteralCommandBuilder<CommandSourceStack>.() -> Unit>()
    internal val clientTestmodCommandBuilders = HashMap<String, LiteralCommandBuilder<ClientCommandSourceStack>.() -> Unit>()

    fun initServer() {
        circleCommand
        commandTestCommand
        sphereCommand
        guiCommand
        itemTestCommand
        persistenceTestCommand
        textTestCommand
        sideboardCommand
        executeCommandTestCommand

        ServerEventTest.init()
        NetworkTest.initServer()

        command("testmod") {
            testmodCommandBuilders.forEach {
                literal(it.key, it.value)
            }
        }
    }
}
