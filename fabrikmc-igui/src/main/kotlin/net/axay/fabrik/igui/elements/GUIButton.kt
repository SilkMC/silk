package net.axay.fabrik.igui.elements

import net.axay.fabrik.igui.ForInventory
import net.axay.fabrik.igui.GUIClickEvent
import net.axay.fabrik.igui.GUIElement
import net.minecraft.item.ItemStack

open class GUIButton<T : ForInventory>(
    private val icon: ItemStack,
    private val action: (GUIClickEvent<T>) -> Unit,
) : GUIElement<T>() {

    final override fun getItemStack(slot: Int) = icon

    override fun onClickElement(clickEvent: GUIClickEvent<T>) {
        clickEvent.bukkitEvent.isCancelled = true
        action(clickEvent)
    }

}
