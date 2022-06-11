package net.axay.silk.igui.elements

import net.axay.silk.igui.GuiElement
import net.axay.silk.igui.events.GuiClickEvent
import net.minecraft.world.item.ItemStack

class GuiFreeSlot(
    private val onClick: (suspend (GuiClickEvent) -> Unit)?,
) : GuiElement {
    override suspend fun getItemStack(slotIndex: Int): ItemStack = ItemStack.EMPTY

    override fun shouldCancel(clickEvent: GuiClickEvent) = false

    override suspend fun onClick(clickEvent: GuiClickEvent) {
        onClick?.invoke(clickEvent)
    }
}
