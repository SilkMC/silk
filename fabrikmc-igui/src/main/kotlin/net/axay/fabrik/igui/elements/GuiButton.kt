package net.axay.fabrik.igui.elements

import net.axay.fabrik.igui.events.GuiClickEvent
import net.axay.fabrik.igui.GuiElement
import net.minecraft.item.ItemStack

open class GuiButton(
    val itemStack: ItemStack,
    private val onClick: (GuiClickEvent) -> Unit,
) : GuiElement {
    override fun getItemStack(slotIndex: Int) = itemStack

    override fun shouldCancel(clickEvent: GuiClickEvent) = true

    override fun onClick(clickEvent: GuiClickEvent) {
        onClick.invoke(clickEvent)
    }
}
