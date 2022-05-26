package net.axay.fabrik.igui.events

import net.axay.fabrik.igui.Gui
import net.axay.fabrik.igui.GuiPlayerEvent
import net.minecraft.world.entity.player.Player

class GuiCloseEvent(
    override val gui: Gui,
    override val player: Player,
) : GuiPlayerEvent
