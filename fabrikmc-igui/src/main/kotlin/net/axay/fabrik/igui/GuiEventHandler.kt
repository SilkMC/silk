package net.axay.fabrik.igui

import net.axay.fabrik.igui.events.GuiClickEvent
import net.axay.fabrik.igui.events.GuiCloseEvent

class GuiEventHandler(
    val onClick: ((GuiClickEvent) -> Unit)?,
    val onClose: ((GuiCloseEvent) -> Unit)?,
)
