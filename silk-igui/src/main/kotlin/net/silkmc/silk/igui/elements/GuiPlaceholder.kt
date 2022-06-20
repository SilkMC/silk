package net.silkmc.silk.igui.elements

import net.silkmc.silk.igui.Gui
import net.silkmc.silk.igui.GuiElement
import net.silkmc.silk.igui.GuiIcon
import net.silkmc.silk.igui.events.GuiClickEvent

open class GuiPlaceholder(
    val icon: GuiIcon,
) : GuiElement {
    override suspend fun getItemStack(slotIndex: Int) = icon.itemStack

    override fun shouldCancel(clickEvent: GuiClickEvent) = true

    override suspend fun onClick(clickEvent: GuiClickEvent) { }

    override fun startUsing(gui: Gui) {
        icon.startUsing(gui)
    }

    override fun stopUsing(gui: Gui) {
        icon.stopUsing(gui)
    }
}
