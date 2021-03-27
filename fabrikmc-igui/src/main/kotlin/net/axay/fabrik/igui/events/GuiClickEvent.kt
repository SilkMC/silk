package net.axay.fabrik.igui.events

import net.axay.fabrik.igui.Gui
import net.axay.fabrik.igui.GuiActionType
import net.axay.fabrik.igui.GuiPlayerEvent
import net.axay.fabrik.igui.GuiSlot
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.screen.slot.Slot

class GuiClickEvent(
    override val gui: Gui,
    override val player: PlayerEntity,
    val type: GuiActionType,
    val slotIndex: Int,
    val slot: Slot?,
    val guiSlot: GuiSlot?,
) : GuiPlayerEvent
