package net.axay.fabrik.igui

import net.axay.fabrik.core.task.coroutineTask

abstract class GuiPageChangeCalculator {

    abstract fun calculateNewPage(currentPage: Int, pages: Collection<Int>): Int?

    object GuiPreviousPageCalculator : GuiPageChangeCalculator() {
        override fun calculateNewPage(currentPage: Int, pages: Collection<Int>) =
            pages.sortedDescending().find { it < currentPage }
    }

    object GuiNextPageCalculator : GuiPageChangeCalculator() {
        override fun calculateNewPage(currentPage: Int, pages: Collection<Int>) =
            pages.sorted().find { it > currentPage }
    }

    class GuiConsistentPageCalculator(private val toPage: Int) : GuiPageChangeCalculator() {
        override fun calculateNewPage(currentPage: Int, pages: Collection<Int>) = toPage
    }

}

enum class PageChangeEffect {
    INSTANT,
    SLIDE_HORIZONTALLY,
    SLIDE_VERTICALLY,
    SWIPE_HORIZONTALLY,
    SWIPE_VERTICALLY,
}

enum class InventoryChangeEffect(
    val effect: PageChangeEffect
) {
    INSTANT(PageChangeEffect.INSTANT)
}

internal fun GuiInstance.changePage(
    effect: PageChangeEffect,
    fromPage: GuiPage,
    toPage: GuiPage,
) {

    val fromPageInt = fromPage.number
    val toPageInt = toPage.number

    when (effect) {

        PageChangeEffect.INSTANT -> loadPageUnsafe(toPage)

        PageChangeEffect.SLIDE_HORIZONTALLY -> {

            val width = gui.data.guiType.dimensions.width

            changePageEffect(fromPageInt, toPageInt, width) { currentOffset, ifInverted ->
                if (ifInverted) {
                    loadPageUnsafe(fromPage, offsetHorizontally = currentOffset)
                    loadPageUnsafe(toPage, offsetHorizontally = -(width - currentOffset))
                } else {
                    loadPageUnsafe(fromPage, offsetHorizontally = -currentOffset)
                    loadPageUnsafe(toPage, offsetHorizontally = width - currentOffset)
                }
            }

        }

        PageChangeEffect.SLIDE_VERTICALLY -> {

            val height = gui.data.guiType.dimensions.height

            changePageEffect(fromPageInt, toPageInt, height) { currentOffset, ifInverted ->
                if (ifInverted) {
                    loadPageUnsafe(fromPage, offsetVertically = currentOffset)
                    loadPageUnsafe(toPage, offsetVertically = -(height - currentOffset))
                } else {
                    loadPageUnsafe(fromPage, offsetVertically = -currentOffset)
                    loadPageUnsafe(toPage, offsetVertically = height - currentOffset)
                }
            }

        }

        PageChangeEffect.SWIPE_HORIZONTALLY -> {

            val width = gui.data.guiType.dimensions.width

            changePageEffect(fromPageInt, toPageInt, width) { currentOffset, ifInverted ->
                if (ifInverted) {
                    loadPageUnsafe(toPage, offsetHorizontally = -(width - currentOffset))
                } else {
                    loadPageUnsafe(toPage, offsetHorizontally = width - currentOffset)
                }
            }

        }

        PageChangeEffect.SWIPE_VERTICALLY -> {

            val height = gui.data.guiType.dimensions.height

            changePageEffect(fromPageInt, toPageInt, height) { currentOffset, ifInverted ->
                if (ifInverted) {
                    loadPageUnsafe(toPage, offsetVertically = -(height - currentOffset))
                } else {
                    loadPageUnsafe(toPage, offsetVertically = height - currentOffset)
                }
            }

        }

    }
}

internal fun GuiInstance.changeGui(
    effect: InventoryChangeEffect,
    fromPage: GuiPage,
    toPage: GuiPage
) = changePage(effect.effect, fromPage, toPage)

private inline fun changePageEffect(
    fromPage: Int,
    toPage: Int,
    doFor: Int,
    crossinline effect: (currentOffset: Int, ifInverted: Boolean) -> Unit,
) {

    val ifInverted = fromPage >= toPage

    var currentOffset = 1
    coroutineTask(
        period = 50,
        howOften = doFor.toLong()
    ) {

        effect.invoke(currentOffset, ifInverted)

        currentOffset++

    }

}
