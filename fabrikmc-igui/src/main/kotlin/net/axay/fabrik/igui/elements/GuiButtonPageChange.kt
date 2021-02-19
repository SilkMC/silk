package net.axay.fabrik.igui.elements

import net.axay.fabrik.igui.GuiClickEvent
import net.axay.fabrik.igui.GuiPageChangeCalculator
import net.axay.fabrik.igui.PageChangeEffect
import net.axay.fabrik.igui.changePage
import net.minecraft.item.ItemStack

class GuiButtonPageChange(
    icon: ItemStack,
    calculator: GuiPageChangeCalculator,
    onChange: ((GuiClickEvent) -> Unit)?
) : GuiButton(icon, {

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
