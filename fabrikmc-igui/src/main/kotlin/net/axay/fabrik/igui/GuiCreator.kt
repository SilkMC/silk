package net.axay.fabrik.igui

abstract class GuiCreator {
    abstract fun createInstance(guiData: GuiData): Gui
}

class SharedGuiCreator : GuiCreator() {
    override fun createInstance(guiData: GuiData) = GuiShared(guiData)
}

class IndividualGuiCreator(
    private val resetOnClose: Boolean = true,
    private val resetOnQuit: Boolean = true
) : GuiCreator() {
    override fun createInstance(guiData: GuiData) = GuiIndividual(guiData, resetOnClose, resetOnQuit)
}
