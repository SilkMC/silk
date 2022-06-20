package net.silkmc.silk.igui

import net.silkmc.silk.igui.events.GuiClickEvent
import net.silkmc.silk.igui.events.GuiCloseEvent

class GuiEventHandler(
    val onClick: (suspend (GuiClickEvent) -> Unit)?,
    val onClose: (suspend (GuiCloseEvent) -> Unit)?,
)
