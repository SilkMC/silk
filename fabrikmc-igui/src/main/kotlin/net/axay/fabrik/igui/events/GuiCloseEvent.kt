package net.axay.fabrik.igui.events

import net.axay.fabrik.igui.Gui
import net.axay.fabrik.igui.GuiPlayerEvent
import net.minecraft.entity.player.PlayerEntity

class GuiCloseEvent(
    override val gui: Gui,
    override val player: PlayerEntity,
) : GuiPlayerEvent
