package net.axay.fabrik.igui

import net.axay.fabrik.igui.events.GuiClickEvent
import net.minecraft.item.ItemStack

interface GuiElement {
    fun getItemStack(slotIndex: Int): ItemStack

    fun shouldCancel(clickEvent: GuiClickEvent): Boolean

    fun onClick(clickEvent: GuiClickEvent)

    fun startUsing(gui: Gui) { }

    fun stopUsing(gui: Gui) { }
}
