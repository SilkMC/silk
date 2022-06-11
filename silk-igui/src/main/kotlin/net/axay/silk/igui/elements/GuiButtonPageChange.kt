package net.axay.silk.igui.elements

import net.axay.silk.igui.Gui
import net.axay.silk.igui.GuiIcon
import net.axay.silk.igui.GuiPage
import net.axay.silk.igui.changePage
import net.axay.silk.igui.events.GuiClickEvent

class GuiButtonPageChange(
    icon: GuiIcon,
    calculator: Calculator,
    shouldChange: suspend (GuiClickEvent) -> Boolean,
    onChange: (suspend (GuiClickEvent) -> Unit)?,
) : GuiButton(
    icon,
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
