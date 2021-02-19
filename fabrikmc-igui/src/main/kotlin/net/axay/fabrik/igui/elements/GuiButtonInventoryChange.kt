package net.axay.fabrik.igui.elements

import net.axay.fabrik.igui.*
import net.minecraft.item.ItemStack

class GuiButtonInventoryChange(
    icon: ItemStack,
    changeToGuiCallback: () -> Gui,
    changeToPageInt: Int?,
    onChange: ((GuiClickEvent) -> Unit)?
) : GuiButton(icon, {

    val changeToGui = changeToGuiCallback.invoke().getInstance(it.player)

    val effect = (changeToGui.gui.data.transitionTo ?: it.guiInstance.gui.data.transitionFrom)
        ?: InventoryChangeEffect.INSTANT

    val changeToPage = changeToGui.getPage(changeToPageInt) ?: changeToGui.currentPage

    changeToGui.changeGui(effect, it.guiInstance.currentPage, changeToPage)

    it.player.openGuiInstance(changeToGui)

    onChange?.invoke(it)

})
