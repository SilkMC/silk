package net.axay.fabrik.igui

class GuiPage(
    val number: Int,
    internal val slots: Map<Int, GuiSlot>,
    val transitionTo: PageChangeEffect?,
    val transitionFrom: PageChangeEffect?
)
