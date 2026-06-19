package net.silkmc.silk.test.igui

import net.minecraft.world.item.Items
import net.silkmc.silk.core.text.literal
import net.silkmc.silk.igui.*

object OtherTestGui {
    fun create(type: GuiType = GuiType.THREE_BY_THREE) = igui(type, "Byecraft".literal, 1) {
        page(1) {
            effectFrom = GuiPage.ChangeEffect.SLIDE_HORIZONTALLY
            effectTo = GuiPage.ChangeEffect.SLIDE_HORIZONTALLY

            placeholder(Slots.All, Items.DIAMOND.guiIcon)
            nextPage(1 sl 1, Items.EMERALD.guiIcon)
        }

        page {
            placeholder(Slots.All, Items.EMERALD.guiIcon)
            previousPage(1 sl 1, Items.DIAMOND.guiIcon)
        }
    }
}
