package net.axay.fabrik.igui

import net.axay.fabrik.core.task.coroutineTask

fun Gui.changePage(
    fromPage: GuiPage,
    toPage: GuiPage,
    overrideEffect: GuiPage.ChangeEffect? = null,
) {
    val effect = overrideEffect
        ?: ((toPage.effectTo ?: fromPage.effectFrom) ?: GuiPage.ChangeEffect.INSTANT)

    val inverted = fromPage.number > toPage.number

    when (effect) {
        GuiPage.ChangeEffect.INSTANT -> loadPage(toPage)
        GuiPage.ChangeEffect.SLIDE_HORIZONTALLY -> {
            changePageEffect(guiType.dimensions.width, inverted) { offset, offsetOpposite ->
                loadPage(fromPage, offsetHorizontally = offset)
                loadPage(toPage, offsetHorizontally = offsetOpposite)
            }
        }
        GuiPage.ChangeEffect.SLIDE_VERTICALLY -> {
            changePageEffect(guiType.dimensions.height, inverted) { offset, offsetOpposite ->
                loadPage(fromPage, offsetVertically = offset)
                loadPage(toPage, offsetVertically = offsetOpposite)
            }
        }
        GuiPage.ChangeEffect.SWIPE_HORIZONTALLY -> {
            changePageEffect(guiType.dimensions.width, inverted) { _, offsetOpposite ->
                loadPage(toPage, offsetHorizontally = offsetOpposite)
            }
        }
        GuiPage.ChangeEffect.SWIPE_VERTICALLY -> {
            changePageEffect(guiType.dimensions.height, inverted) { _, offsetOpposite ->
                loadPage(toPage, offsetVertically = offsetOpposite)
            }
        }
    }
}

private inline fun changePageEffect(
    effectLength: Int,
    inverted: Boolean,
    crossinline effect: (offset: Int, offsetOpposite: Int) -> Unit
) {
    var currentOffset = 1
    coroutineTask(
        period = 50,
        howOften = effectLength.toLong()
    ) {
        effect.invoke(
            if (inverted) currentOffset else -currentOffset,
            if (inverted) -(effectLength - currentOffset) else (effectLength - currentOffset)
        )
        currentOffset++
    }
}
