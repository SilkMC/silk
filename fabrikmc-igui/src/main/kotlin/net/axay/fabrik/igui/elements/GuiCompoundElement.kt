package net.axay.fabrik.igui.elements

import net.axay.fabrik.igui.Gui
import net.axay.fabrik.igui.GuiCompound
import net.axay.fabrik.igui.GuiElement
import net.axay.fabrik.igui.events.GuiClickEvent

class GuiCompoundElement(
    val compound: GuiCompound<*>
) : GuiElement {
    override fun getItemStack(slotIndex: Int) = compound.getItemStack(slotIndex)

    override fun shouldCancel(clickEvent: GuiClickEvent) = true

    override fun onClick(clickEvent: GuiClickEvent) = compound.onClickElement(clickEvent)

    override fun startUsing(gui: Gui) {
        compound.startUsing(gui)
    }

    override fun stopUsing(gui: Gui) {
        compound.stopUsing(gui)
    }
}
