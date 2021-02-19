package net.axay.fabrik.igui

class GUIPage(
    val number: Int,
    internal val slots: Map<Int, GUISlot>,
    val transitionTo: PageChangeEffect?,
    val transitionFrom: PageChangeEffect?
)
