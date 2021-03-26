package net.axay.fabrik.test.gui

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.axay.fabrik.core.task.fabrikCoroutineScope
import net.axay.fabrik.core.text.literal
import net.axay.fabrik.igui.*
import net.axay.fabrik.igui.observable.GuiProperty
import net.axay.fabrik.igui.observable.toGuiList
import net.minecraft.item.Items
import net.minecraft.util.registry.Registry
import java.util.*

object SimpleTestGui {
    val gui = {
        igui(GuiType.NINE_BY_SIX, "Moincraft".literal, 1) {
            page(1) {
                effectFrom = GuiPage.ChangeEffect.SLIDE_HORIZONTALLY
                effectTo = GuiPage.ChangeEffect.SLIDE_HORIZONTALLY

                placeholder(Slots.All, Items.WHITE_STAINED_GLASS_PANE.guiIcon)

                val changingNameProperty = GuiProperty("Ausgangsstring")
                var changingName by changingNameProperty

                placeholder(2 sl 5, changingNameProperty.guiIcon {
                    listOf(Items.ACACIA_SIGN, Items.BIRCH_SIGN, Items.DARK_OAK_SIGN)
                        .random().defaultStack.setCustomName(it.literal)
                })

                fabrikCoroutineScope.launch {
                    repeat(30) {
                        delay(500)
                        changingName = UUID.randomUUID().toString()
                    }
                }

                nextPage(2 sl 9, Items.PAPER.guiIcon)
            }

            page {
                effectFrom = GuiPage.ChangeEffect.SLIDE_VERTICALLY

                placeholder(Slots.All, Items.BLACK_STAINED_GLASS_PANE.guiIcon)

                previousPage(2 sl 1, Items.PAPER.guiIcon)

                nextPage(3 sl 5, Items.STICK.guiIcon)
            }

            page {
                effectFrom = GuiPage.ChangeEffect.SLIDE_VERTICALLY

                placeholder(Slots.ColumnOne, Items.POLISHED_ANDESITE.guiIcon)
                placeholder(Slots.ColumnNine, Items.POLISHED_ANDESITE.guiIcon)

                previousPage(1 sl 1, Items.STICK.guiIcon)

                val compound = compound(
                    (1 sl 2) rectTo (6 sl 8),
                    Registry.ITEM.filter { it != Items.AIR }.toGuiList(),
                    iconGenerator = { it.defaultStack }
                )

                compoundScrollForwards(1 sl 9, Items.NETHERITE_BLOCK.guiIcon, compound)
                compoundScrollBackwards(6 sl 9, Items.NETHERITE_BLOCK.guiIcon, compound)
            }
        }
    }
}
