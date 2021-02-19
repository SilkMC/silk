package net.axay.fabrik.igui

import net.minecraft.entity.player.PlayerEntity

class GuiClickEvent(
    val guiInstance: GuiInstance,
    val player: PlayerEntity,
    val type: GuiActionType,
    val slotIndex: Int
)
