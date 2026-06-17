package net.silkmc.silk.test.igui

import net.minecraft.world.item.Items
import net.silkmc.silk.core.text.literal
import net.silkmc.silk.igui.*

object OtherTestGui {
    fun create() = igui(GuiType.THREE_BY_THREE, "Byecraft".literal, 1) {
        page(1) {
            effectFrom = GuiPage.ChangeEffect.SLIDE_HORIZONTALLY
            effectTo = GuiPage.ChangeEffect.SLIDE_HORIZONTALLY

            placeholder(Slots.All, Items.DIAMOND.guiIcon)
            nextPage(2 sl 2, Items.EMERALD.guiIcon)
        }

        page {
            placeholder(Slots.All, Items.EMERALD.guiIcon)
            previousPage(2 sl 2, Items.DIAMOND.guiIcon)
        }
    }
}
