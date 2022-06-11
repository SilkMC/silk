package net.axay.silk.igui.events

import net.axay.silk.igui.Gui
import net.axay.silk.igui.GuiActionType
import net.axay.silk.igui.GuiPlayerEvent
import net.axay.silk.igui.GuiSlot
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
