package net.axay.fabrik.igui

class GuiPage(
    val key: String,
    val number: Int,
    val content: Map<Int, GuiElement>,
    val effectTo: ChangeEffect?,
    val effectFrom: ChangeEffect?,
) {
    enum class ChangeEffect {
        INSTANT,
        SLIDE_HORIZONTALLY,
        SLIDE_VERTICALLY,
        SWIPE_HORIZONTALLY,
        SWIPE_VERTICALLY,
    }

    internal fun startUsing(gui: Gui) = content.values.toHashSet().forEach { it.startUsing(gui) }

    internal fun stopUsing(gui: Gui) = content.values.toHashSet().forEach { it.stopUsing(gui) }
}
