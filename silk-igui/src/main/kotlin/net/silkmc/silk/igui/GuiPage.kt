package net.silkmc.silk.igui

class GuiPage(
    val key: String,
    val number: Int,
    val content: Map<Int, GuiElement>,
    val effectTo: ChangeEffect?,
    val effectFrom: ChangeEffect?,
) : GuiUseable() {
    enum class ChangeEffect {
        INSTANT,
        SLIDE_HORIZONTALLY,
        SLIDE_VERTICALLY,
        SWIPE_HORIZONTALLY,
        SWIPE_VERTICALLY,
    }

    override fun startUsing(gui: Gui) {
        content.values.toHashSet().forEach { it.startUsing(gui) }
        super.startUsing(gui)
    }

    override fun stopUsing(gui: Gui) {
        content.values.toHashSet().forEach { it.stopUsing(gui) }
        super.stopUsing(gui)
    }
}
