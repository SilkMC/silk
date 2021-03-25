package net.axay.fabrik.test.gui

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.axay.fabrik.core.task.fabrikCoroutineScope
import net.axay.fabrik.core.text.literal
import net.axay.fabrik.igui.*
import net.axay.fabrik.igui.observable.GuiProperty
import net.minecraft.item.Items
import java.util.*

object SimpleTestGui {
    val changingNameProperty = GuiProperty("Ausgangsname")
    val changingNameIcon = changingNameProperty.guiIcon {
        Items.ACACIA_SIGN.defaultStack.setCustomName(it.literal)
    }

    var changingName by changingNameProperty

    init {
        fabrikCoroutineScope.launch {
            repeat(100) {
                delay(1000)
                changingName = UUID.randomUUID().toString()
            }
        }
    }

    val gui get() = igui(GuiType.NINE_BY_THREE, "Moincraft".literal, 1) {
        page(1) {
            effectFrom = GuiPage.ChangeEffect.SLIDE_HORIZONTALLY
            effectTo = GuiPage.ChangeEffect.SLIDE_HORIZONTALLY

            placeholder(Slots.All, Items.WHITE_STAINED_GLASS_PANE.guiIcon)

            placeholder(2 sl 5, changingNameIcon)

            nextPage(2 sl 9, Items.PAPER.guiIcon)
        }

        page(2) {
            placeholder(Slots.All, Items.BLACK_STAINED_GLASS_PANE.guiIcon)

            previousPage(2 sl 1, Items.PAPER.guiIcon)
        }
    }
}
