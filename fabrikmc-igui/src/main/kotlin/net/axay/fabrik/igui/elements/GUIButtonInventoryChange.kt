package net.axay.fabrik.igui.elements

import net.axay.fabrik.igui.*
import net.axay.fabrik.igui.changeGUI
import net.axay.kspigot.gui.*
import net.minecraft.item.ItemStack

class GUIButtonInventoryChange<T : ForInventory>(
    icon: ItemStack,
    changeToGUICallback: () -> GUI<*>,
    changeToPageInt: Int?,
    onChange: ((GUIClickEvent<T>) -> Unit)?
) : GUIButton<T>(icon, {

    val changeToGUI = changeToGUICallback.invoke().getInstance(it.player)

    val effect = (changeToGUI.gui.data.transitionTo ?: it.guiInstance.gui.data.transitionFrom)
        ?: InventoryChangeEffect.INSTANT

    val changeToPage = changeToGUI.getPage(changeToPageInt) ?: changeToGUI.currentPage

    changeToGUI.changeGUI(effect, it.guiInstance.currentPage, changeToPage)

    it.player.openGUIInstance(changeToGUI)

    onChange?.invoke(it)

})
