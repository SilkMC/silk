package net.axay.fabrik.igui.events

import net.axay.fabrik.igui.Gui
import net.axay.fabrik.igui.GuiActionType
import net.axay.fabrik.igui.GuiPlayerEvent
import net.axay.fabrik.igui.GuiSlot
import net.minecraft.entity.player.PlayerEntity

class GuiClickEvent(
    override val gui: Gui,
    override val player: PlayerEntity,
    val type: GuiActionType,
    val slotIndex: Int,
    val slot: GuiSlot,
) : GuiPlayerEvent
