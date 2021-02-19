package net.axay.fabrik.igui

abstract class GuiCreator {
    abstract fun createInstance(guiData: GuiData): Gui
}

object SharedGuiCreator : GuiCreator() {
    override fun createInstance(guiData: GuiData) = GuiShared(guiData)
}

object IndividualGuiCreator : GuiCreator() {
    override fun createInstance(guiData: GuiData) = GuiIndividual(guiData)
}
