package net.silkmc.silk.igui.events

import net.minecraft.world.entity.player.Player
import net.silkmc.silk.igui.Gui
import net.silkmc.silk.igui.GuiPlayerEvent

class GuiCloseEvent(
    override val gui: Gui,
    override val player: Player,
) : GuiPlayerEvent
