package net.axay.fabrik.igui

import net.axay.fabrik.igui.events.GuiClickEvent
import net.minecraft.world.item.ItemStack

interface GuiElement {
    suspend fun getItemStack(slotIndex: Int): ItemStack

    fun shouldCancel(clickEvent: GuiClickEvent): Boolean

    suspend fun onClick(clickEvent: GuiClickEvent)

    fun startUsing(gui: Gui) { }

    fun stopUsing(gui: Gui) { }
}
