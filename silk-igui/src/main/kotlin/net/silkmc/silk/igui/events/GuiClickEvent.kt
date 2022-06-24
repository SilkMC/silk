package net.silkmc.silk.igui.events

import net.minecraft.world.entity.player.Player
import net.minecraft.world.inventory.Slot
import net.silkmc.silk.igui.Gui
import net.silkmc.silk.igui.GuiActionType
import net.silkmc.silk.igui.GuiPlayerEvent
import net.silkmc.silk.igui.GuiSlot

class GuiClickEvent(
    override val gui: Gui,
    override val player: Player,
    val type: GuiActionType,
    val slotIndex: Int,
    val slot: Slot?,
    val guiSlot: GuiSlot?,
) : GuiPlayerEvent
