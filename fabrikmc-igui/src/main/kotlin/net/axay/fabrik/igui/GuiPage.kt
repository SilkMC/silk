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
}
