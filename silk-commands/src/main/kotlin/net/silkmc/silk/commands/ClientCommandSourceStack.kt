package net.silkmc.silk.commands

import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientLevel
import net.minecraft.client.multiplayer.ClientPacketListener
import net.minecraft.client.multiplayer.ClientSuggestionProvider
import net.minecraft.client.player.LocalPlayer
import net.minecraft.network.chat.Component
import net.silkmc.silk.commands.mixin.client.ClientSuggestionProviderAccessor

typealias ClientCommandSourceStack = ClientSuggestionProvider

val ClientCommandSourceStack.minecraft: Minecraft
    get() = (this as ClientSuggestionProviderAccessor).minecraft

val ClientCommandSourceStack.connection: ClientPacketListener
    get() = (this as ClientSuggestionProviderAccessor).connection

val ClientCommandSourceStack.player: LocalPlayer
    get() = minecraft.player ?: error("Tried to access player while not in game")

val ClientCommandSourceStack.level: ClientLevel
    get() = player.clientLevel

fun ClientCommandSourceStack.sendSuccess(message: Component) {
    sendMessage(message)
}

fun ClientCommandSourceStack.sendFailure(message: Component) {
    sendMessage(Component.empty().append(message).withStyle(ChatFormatting.RED))
}

private fun ClientCommandSourceStack.sendMessage(message: Component) {
    minecraft.gui.chat.addMessage(message)
}
