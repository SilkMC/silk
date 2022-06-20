package net.silkmc.silk.igui

import net.minecraft.world.item.ItemStack
import net.silkmc.silk.igui.events.GuiClickEvent

interface GuiElement {
    suspend fun getItemStack(slotIndex: Int): ItemStack

    fun shouldCancel(clickEvent: GuiClickEvent): Boolean

    suspend fun onClick(clickEvent: GuiClickEvent)

    fun startUsing(gui: Gui) { }

    fun stopUsing(gui: Gui) { }
}
