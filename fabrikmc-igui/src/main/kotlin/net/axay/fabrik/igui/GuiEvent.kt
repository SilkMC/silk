package net.axay.fabrik.igui

import net.minecraft.world.entity.player.Player

interface GuiEvent {
    val gui: Gui
}

interface GuiPlayerEvent : GuiEvent {
    val player: Player
}
