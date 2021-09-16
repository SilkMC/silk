package net.axay.fabrik.igui

import net.axay.fabrik.igui.events.GuiClickEvent
import net.axay.fabrik.igui.events.GuiCloseEvent

class GuiEventHandler(
    val onClick: (suspend (GuiClickEvent) -> Unit)?,
    val onClose: (suspend (GuiCloseEvent) -> Unit)?,
)
