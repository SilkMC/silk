package net.axay.fabrik.igui

abstract class GuiUseable {
    protected val registeredGuis = HashSet<Gui>()

    protected open fun onChangeUseStatus(inUse: Boolean) { }

    internal open fun startUsing(gui: Gui) {
        if (registeredGuis.isEmpty())
            onChangeUseStatus(true)
        registeredGuis += gui
    }

    internal open fun stopUsing(gui: Gui) {
        registeredGuis -= gui
        if (registeredGuis.isEmpty())
            onChangeUseStatus(false)
    }
}
