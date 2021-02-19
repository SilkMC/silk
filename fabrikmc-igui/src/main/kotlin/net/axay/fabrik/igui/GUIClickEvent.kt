package net.axay.fabrik.igui

import net.minecraft.entity.player.PlayerEntity

class GUIClickEvent(
    val guiInstance: GUIInstance,
    val player: PlayerEntity,
    val type: GUIActionType,
    val slotIndex: Int
)
