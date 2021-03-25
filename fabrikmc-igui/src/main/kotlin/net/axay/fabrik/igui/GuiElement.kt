package net.axay.fabrik.igui

import net.axay.fabrik.igui.events.GuiClickEvent
import net.minecraft.item.ItemStack

abstract class GuiElement {
    abstract val itemStack: ItemStack

    abstract fun shouldCancel(clickEvent: GuiClickEvent): Boolean

    abstract fun onClick(clickEvent: GuiClickEvent)
}
