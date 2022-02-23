package net.axay.fabrik.igui.events

import net.axay.fabrik.igui.Gui
import net.axay.fabrik.igui.GuiActionType
import net.axay.fabrik.igui.GuiPlayerEvent
import net.axay.fabrik.igui.GuiSlot
import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.Slot

class GuiClickEvent(
    override val gui: Gui,
    override val player: Player,
    val type: GuiActionType,
    val slotIndex: Int,
    val slot: Slot?,
    val guiSlot: GuiSlot?,
) : GuiPlayerEvent
