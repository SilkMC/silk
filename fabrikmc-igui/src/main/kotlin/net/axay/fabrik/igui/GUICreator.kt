package net.axay.fabrik.igui

abstract class GUICreator {
    abstract fun createInstance(guiData: GUIData): GUI
}

class SharedGUICreator : GUICreator() {
    override fun createInstance(guiData: GUIData) = GUIShared(guiData)
}

class IndividualGUICreator(
    private val resetOnClose: Boolean = true,
    private val resetOnQuit: Boolean = true
) : GUICreator() {
    override fun createInstance(guiData: GUIData) = GUIIndividual(guiData, resetOnClose, resetOnQuit)
}
