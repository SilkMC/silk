package net.axay.fabrik.igui

import net.minecraft.item.ItemStack

abstract class GuiSlot {
    abstract fun shouldCancel(clickEvent: GuiClickEvent): Boolean
    abstract fun onClick(clickEvent: GuiClickEvent)
}

// ELEMENT

abstract class GuiElement : GuiSlot() {

    abstract fun getItemStack(slot: Int): ItemStack

    final override fun onClick(clickEvent: GuiClickEvent) {
        clickEvent.guiInstance.gui.data.generalOnClick?.invoke(clickEvent)
        onClickElement(clickEvent)
    }

    protected abstract fun onClickElement(clickEvent: GuiClickEvent)

    internal open fun startUsing(gui: GuiInstance) { }
    internal open fun stopUsing(gui: GuiInstance) { }

}
