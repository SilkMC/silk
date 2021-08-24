package net.axay.fabrik.test.commands

import net.axay.fabrik.commands.*
import net.axay.fabrik.core.text.literal
import net.axay.fabrik.igui.openGui
import net.axay.fabrik.test.gui.SimpleTestGui

private val guis = mapOf(
    "simpletestgui" to SimpleTestGui.gui
)

val guiCommand = command("gui", true) {
    literal("open") {
        argument<String>("guiname") {
            simpleSuggests { guis.keys }
            simpleExecutes {
                val gui = guis[resolveArgument()]
                if (gui != null)
                    source.player.openGui(gui.invoke(), 1)
                else source.sendError("This GUI does not exist!".literal)
            }
        }
    }
}
