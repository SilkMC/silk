package net.axay.fabrik.igui.elements

import net.axay.fabrik.igui.GUIClickEvent
import net.axay.fabrik.igui.GUIPageChangeCalculator
import net.axay.fabrik.igui.PageChangeEffect
import net.axay.fabrik.igui.changePage
import net.minecraft.item.ItemStack

class GUIButtonPageChange(
    icon: ItemStack,
    calculator: GUIPageChangeCalculator,
    onChange: ((GUIClickEvent) -> Unit)?
) : GUIButton(icon, {

    val currentPage = it.guiInstance.currentPage
    val newPage = it.guiInstance.getPage(
        calculator.calculateNewPage(
            it.guiInstance.currentPageInt,
            it.guiInstance.gui.data.pages.keys
        )
    )
    if (newPage != null) {

        val effect = (newPage.transitionTo ?: currentPage.transitionFrom)
            ?: PageChangeEffect.INSTANT

        it.guiInstance.changePage(effect, currentPage, newPage)

        onChange?.invoke(it)

    }

})
