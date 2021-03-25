package net.axay.fabrik.igui

import net.minecraft.entity.player.PlayerEntity

interface GuiEvent {
    val gui: Gui
}

interface GuiPlayerEvent : GuiEvent {
    val player: PlayerEntity
}
