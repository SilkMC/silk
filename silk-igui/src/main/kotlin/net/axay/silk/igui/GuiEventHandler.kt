package net.axay.silk.igui

import net.axay.silk.igui.events.GuiClickEvent
import net.axay.silk.igui.events.GuiCloseEvent

class GuiEventHandler(
    val onClick: (suspend (GuiClickEvent) -> Unit)?,
    val onClose: (suspend (GuiCloseEvent) -> Unit)?,
)
