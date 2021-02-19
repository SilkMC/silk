package net.axay.fabrik.igui.elements

import net.axay.fabrik.igui.GUIClickEvent
import net.axay.fabrik.igui.GUIElement
import net.minecraft.item.ItemStack

open class GUIButton(
    private val icon: ItemStack,
    private val action: (GUIClickEvent) -> Unit,
) : GUIElement() {

    final override fun getItemStack(slot: Int) = icon

    override fun shouldCancel(clickEvent: GUIClickEvent) = true

    override fun onClickElement(clickEvent: GUIClickEvent) {
        action(clickEvent)
    }

}
