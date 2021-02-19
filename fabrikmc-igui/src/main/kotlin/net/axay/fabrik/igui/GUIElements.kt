package net.axay.fabrik.igui

import net.minecraft.item.ItemStack

abstract class GUISlot {
    abstract fun shouldCancel(clickEvent: GUIClickEvent): Boolean

    abstract fun onClick(clickEvent: GUIClickEvent)
}

// ELEMENT

abstract class GUIElement : GUISlot() {

    abstract fun getItemStack(slot: Int): ItemStack

    final override fun onClick(clickEvent: GUIClickEvent) {
        clickEvent.guiInstance.gui.data.generalOnClick?.invoke(clickEvent)
        onClickElement(clickEvent)
    }

    protected abstract fun onClickElement(clickEvent: GUIClickEvent)

    internal open fun startUsing(gui: GUIInstance) { }
    internal open fun stopUsing(gui: GUIInstance) { }

}
