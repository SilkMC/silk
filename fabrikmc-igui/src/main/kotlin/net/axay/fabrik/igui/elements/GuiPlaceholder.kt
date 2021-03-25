package net.axay.fabrik.igui.elements

import net.axay.fabrik.igui.GuiElement
import net.axay.fabrik.igui.events.GuiClickEvent
import net.minecraft.item.ItemStack

class GuiPlaceholder(
    val itemStack: ItemStack,
) : GuiElement {
    override fun getItemStack(slotIndex: Int) = itemStack

    override fun shouldCancel(clickEvent: GuiClickEvent) = true

    override fun onClick(clickEvent: GuiClickEvent) { }
}
