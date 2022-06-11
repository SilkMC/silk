package net.axay.silk.igui.events

import net.axay.silk.igui.Gui
import net.axay.silk.igui.GuiPlayerEvent
import net.minecraft.world.entity.player.Player

class GuiCloseEvent(
    override val gui: Gui,
    override val player: Player,
) : GuiPlayerEvent
