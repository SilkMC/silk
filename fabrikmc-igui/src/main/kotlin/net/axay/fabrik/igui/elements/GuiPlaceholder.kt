package net.axay.fabrik.igui.elements

import net.axay.fabrik.igui.GuiClickEvent
import net.axay.fabrik.igui.GuiElement
import net.minecraft.item.ItemStack

class GuiPlaceholder(
    private val icon: ItemStack
) : GuiElement() {

    override fun getItemStack(slot: Int) = icon

    override fun shouldCancel(clickEvent: GuiClickEvent) = true

    override fun onClickElement(clickEvent: GuiClickEvent) { }

}
