package net.axay.fabrik.test.gui

import net.axay.fabrik.igui.*
import net.minecraft.item.ItemStack
import net.minecraft.item.Items

object SimpleTestGui {
    val gui get() = igui(GuiType.NINE_BY_THREE, "Moincraft", "1") {
        page(1) {
            effectFrom = GuiPage.ChangeEffect.SLIDE_HORIZONTALLY
            effectTo = GuiPage.ChangeEffect.SLIDE_HORIZONTALLY

            placeholder(Slots.All, ItemStack(Items.WHITE_STAINED_GLASS_PANE))

            nextPage(2 sl 9, ItemStack(Items.PAPER))
        }

        page(2) {
            placeholder(Slots.All, ItemStack(Items.BLACK_STAINED_GLASS_PANE))

            previousPage(2 sl 1, ItemStack(Items.PAPER))
        }
    }
}
