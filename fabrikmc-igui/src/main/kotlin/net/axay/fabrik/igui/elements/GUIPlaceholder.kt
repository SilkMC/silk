package net.axay.fabrik.igui.elements

import net.axay.fabrik.igui.ForInventory
import net.axay.fabrik.igui.GUIClickEvent
import net.axay.fabrik.igui.GUIElement
import net.minecraft.item.ItemStack

class GUIPlaceholder<T : ForInventory>(
    private val icon: ItemStack
) : GUIElement<T>() {

    override fun getItemStack(slot: Int) = icon

    override fun onClickElement(clickEvent: GUIClickEvent<T>) {
        clickEvent.bukkitEvent.isCancelled = true
    }

}
