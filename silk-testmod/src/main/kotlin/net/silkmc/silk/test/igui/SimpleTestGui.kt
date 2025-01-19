package net.silkmc.silk.test.igui

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.minecraft.core.component.DataComponents
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.Items
import net.silkmc.silk.core.Silk
import net.silkmc.silk.core.annotations.DelicateSilkApi
import net.silkmc.silk.core.task.silkCoroutineScope
import net.silkmc.silk.core.text.literal
import net.silkmc.silk.igui.*
import net.silkmc.silk.igui.observable.GuiProperty
import net.silkmc.silk.igui.observable.toGuiList
import java.util.*

@OptIn(DelicateSilkApi::class)
object SimpleTestGui {
    fun create() = igui(GuiType.NINE_BY_SIX, "Moincraft".literal, 1) {
        page(1) {
            effectFrom = GuiPage.ChangeEffect.SLIDE_HORIZONTALLY
            effectTo = GuiPage.ChangeEffect.SLIDE_HORIZONTALLY

            placeholder(Slots.All, Items.WHITE_STAINED_GLASS_PANE.guiIcon)

            val changingName = GuiProperty("Ausgangsstring")

            placeholder(2 sl 5, changingName.guiIcon {
                listOf(Items.ACACIA_SIGN, Items.BIRCH_SIGN, Items.DARK_OAK_SIGN)
                    .random().defaultInstance.apply { set(DataComponents.CUSTOM_NAME, it.literal) }
            })

            silkCoroutineScope.launch {
                repeat(30) {
                    delay(500)
                    changingName.set(UUID.randomUUID().toString())
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
                Silk.server?.registryAccess()
                    ?.lookupOrThrow(Registries.ITEM)
                    ?.filter { it != Items.AIR }
                    .orEmpty().toGuiList(),
                iconGenerator = { it.defaultInstance }
            )

            compoundScrollForwards(1 sl 9, Items.NETHERITE_BLOCK.guiIcon, compound)
            compoundScrollBackwards(6 sl 9, Items.NETHERITE_BLOCK.guiIcon, compound)
        }
    }
}
