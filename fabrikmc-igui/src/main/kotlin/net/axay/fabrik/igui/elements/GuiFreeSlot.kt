package net.axay.fabrik.igui.elements

import net.axay.fabrik.igui.GuiClickEvent
import net.axay.fabrik.igui.GuiSlot

class GuiFreeSlot : GuiSlot() {
    override fun onClick(clickEvent: GuiClickEvent) { }
    override fun shouldCancel(clickEvent: GuiClickEvent) = false
}
