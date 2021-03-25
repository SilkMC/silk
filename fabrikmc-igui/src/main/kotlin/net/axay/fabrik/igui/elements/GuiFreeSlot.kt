package net.axay.fabrik.igui.elements

import net.axay.fabrik.igui.GuiElement
import net.axay.fabrik.igui.events.GuiClickEvent
import net.minecraft.item.ItemStack

class GuiFreeSlot(
    private val onClick: ((GuiClickEvent) -> Unit)?,
) : GuiElement() {
    override val itemStack: ItemStack = ItemStack.EMPTY

    override fun shouldCancel(clickEvent: GuiClickEvent) = false

    override fun onClick(clickEvent: GuiClickEvent) {
        onClick?.invoke(clickEvent)
    }
}
