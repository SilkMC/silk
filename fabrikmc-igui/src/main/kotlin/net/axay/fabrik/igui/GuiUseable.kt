package net.axay.fabrik.igui

abstract class GuiUseable {
    protected val registeredGuis = HashSet<Gui>()

    var inUse = false
        private set

    protected open fun onChangeUseStatus(inUse: Boolean) { }

    internal open fun startUsing(gui: Gui) {
        if (registeredGuis.isEmpty()) {
            inUse = true
            onChangeUseStatus(true)
        }
        registeredGuis += gui
    }

    internal open fun stopUsing(gui: Gui) {
        registeredGuis -= gui
        if (registeredGuis.isEmpty()) {
            inUse = false
            onChangeUseStatus(false)
        }
    }
}
