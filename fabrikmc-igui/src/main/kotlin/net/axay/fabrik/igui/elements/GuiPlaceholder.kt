package net.axay.fabrik.igui.elements

import net.axay.fabrik.igui.Gui
import net.axay.fabrik.igui.GuiElement
import net.axay.fabrik.igui.GuiIcon
import net.axay.fabrik.igui.events.GuiClickEvent

open class GuiPlaceholder(
    val icon: GuiIcon,
) : GuiElement {
    override fun getItemStack(slotIndex: Int) = icon.itemStack

    override fun shouldCancel(clickEvent: GuiClickEvent) = true

    override suspend fun onClick(clickEvent: GuiClickEvent) { }

    override fun startUsing(gui: Gui) {
        icon.startUsing(gui)
    }

    override fun stopUsing(gui: Gui) {
        icon.stopUsing(gui)
    }
}
