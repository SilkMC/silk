package net.axay.fabrik.igui.elements

import net.axay.fabrik.igui.ForInventory
import net.axay.fabrik.igui.GUIClickEvent
import net.axay.fabrik.igui.GUISlot

class GUIFreeSlot<T : ForInventory> : GUISlot<T>() {
    override fun onClick(clickEvent: GUIClickEvent<T>) {
        /* do nothing */
    }
}
