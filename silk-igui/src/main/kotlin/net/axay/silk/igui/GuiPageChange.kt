package net.axay.silk.igui

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.axay.silk.core.task.mcCoroutineScope

fun Gui.changePage(
    fromPage: GuiPage,
    toPage: GuiPage,
    overrideEffect: GuiPage.ChangeEffect? = null,
) = mcCoroutineScope.launch {
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

private suspend inline fun changePageEffect(
    effectLength: Int,
    inverted: Boolean,
    crossinline effect: suspend (offset: Int, offsetOpposite: Int) -> Unit
) {
    var currentOffset = 1
    repeat(effectLength) {
        effect.invoke(
            if (inverted) currentOffset else -currentOffset,
            if (inverted) -(effectLength - currentOffset) else (effectLength - currentOffset)
        )
        currentOffset++

        delay(50)
    }
}
