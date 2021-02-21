package net.axay.fabrik.test.gui

import net.axay.fabrik.igui.GuiType
import net.axay.fabrik.igui.PageChangeEffect
import net.axay.fabrik.igui.Slots
import net.axay.fabrik.igui.igui
import net.minecraft.item.ItemStack
import net.minecraft.item.Items

object SimpleTestGui {
    val gui get() = igui(GuiType.NINE_BY_THREE) {
        title = "Moincraft"
        defaultPage = 1

        page(1) {
            transitionFrom = PageChangeEffect.SLIDE_HORIZONTALLY
            transitionTo = PageChangeEffect.SLIDE_HORIZONTALLY

            placeholder(Slots.All, ItemStack(Items.WHITE_STAINED_GLASS_PANE))

            nextPage(Slots.ColumnNine, ItemStack(Items.PAPER))
        }

        page(2) {

            placeholder(Slots.All, ItemStack(Items.BLACK_STAINED_GLASS_PANE))

            previousPage(Slots.ColumnOne, ItemStack(Items.PAPER))
        }
    }
}
