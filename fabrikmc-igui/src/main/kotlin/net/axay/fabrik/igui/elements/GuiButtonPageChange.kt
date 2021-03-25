package net.axay.fabrik.igui.elements

import net.axay.fabrik.igui.Gui
import net.axay.fabrik.igui.GuiPage
import net.axay.fabrik.igui.changePage
import net.axay.fabrik.igui.events.GuiClickEvent
import net.minecraft.item.ItemStack

class GuiButtonPageChange(
    itemStack: ItemStack,
    calculator: Calculator,
    shouldChange: (GuiClickEvent) -> Boolean,
    onChange: ((GuiClickEvent) -> Unit)?,
) : GuiButton(
    itemStack,
    { event ->
        val newPage = calculator.calculateNewPage(event.gui)
        if (newPage != null && shouldChange.invoke(event)) {
            event.gui.changePage(event.gui.currentPage, newPage)
            onChange?.invoke(event)
        }
    }
) {
    interface Calculator {
        fun calculateNewPage(gui: Gui): GuiPage?

        object PreviousPage : Calculator {
            override fun calculateNewPage(gui: Gui) =
                gui.pagesByNumber[gui.pagesByNumber.keys.sortedDescending().find { it < gui.currentPage.number }]
        }

        object NextPage : Calculator {
            override fun calculateNewPage(gui: Gui) =
                gui.pagesByNumber[gui.pagesByNumber.keys.sorted().find { it > gui.currentPage.number }]
        }

        class StaticPageNumber(val pageNumber: Int) : Calculator {
            override fun calculateNewPage(gui: Gui) = gui.pagesByNumber[pageNumber]
        }

        class StaticPageKey(pageKey: Any) : Calculator {
            private val key = pageKey.toString()
            override fun calculateNewPage(gui: Gui) = gui.pagesByKey[key]
        }
    }
}
