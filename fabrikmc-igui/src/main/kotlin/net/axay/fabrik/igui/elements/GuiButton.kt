package net.axay.fabrik.igui.elements

import net.axay.fabrik.igui.GuiClickEvent
import net.axay.fabrik.igui.GuiElement
import net.minecraft.item.ItemStack

open class GuiButton(
    private val icon: ItemStack,
    private val action: (GuiClickEvent) -> Unit,
) : GuiElement() {

    final override fun getItemStack(slot: Int) = icon

    override fun shouldCancel(clickEvent: GuiClickEvent) = true

    override fun onClickElement(clickEvent: GuiClickEvent) {
        action(clickEvent)
    }

}
