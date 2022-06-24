package net.silkmc.silk.igui.elements

import net.silkmc.silk.igui.Gui
import net.silkmc.silk.igui.GuiCompound
import net.silkmc.silk.igui.GuiElement
import net.silkmc.silk.igui.events.GuiClickEvent

class GuiCompoundElement(
    val compound: GuiCompound<*>
) : GuiElement {
    override suspend fun getItemStack(slotIndex: Int) = compound.getItemStack(slotIndex)

    override fun shouldCancel(clickEvent: GuiClickEvent) = true

    override suspend fun onClick(clickEvent: GuiClickEvent) = compound.onClickElement(clickEvent)

    override fun startUsing(gui: Gui) {
        compound.startUsing(gui)
    }

    override fun stopUsing(gui: Gui) {
        compound.stopUsing(gui)
    }
}
