package net.axay.fabrik.igui.elements

import net.axay.fabrik.igui.GUIClickEvent
import net.axay.fabrik.igui.GUIElement
import net.minecraft.item.ItemStack

class GUIPlaceholder(
    private val icon: ItemStack
) : GUIElement() {

    override fun getItemStack(slot: Int) = icon

    override fun shouldCancel(clickEvent: GUIClickEvent) = true

    override fun onClickElement(clickEvent: GUIClickEvent) { }

}
