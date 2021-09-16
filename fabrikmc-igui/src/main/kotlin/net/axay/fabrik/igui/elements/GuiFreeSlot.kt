package net.axay.fabrik.igui.elements

import net.axay.fabrik.igui.GuiElement
import net.axay.fabrik.igui.events.GuiClickEvent
import net.minecraft.item.ItemStack

class GuiFreeSlot(
    private val onClick: ((GuiClickEvent) -> Unit)?,
) : GuiElement {
    override fun getItemStack(slotIndex: Int): ItemStack = ItemStack.EMPTY

    override fun shouldCancel(clickEvent: GuiClickEvent) = false

    override suspend fun onClick(clickEvent: GuiClickEvent) {
        onClick?.invoke(clickEvent)
    }
}
