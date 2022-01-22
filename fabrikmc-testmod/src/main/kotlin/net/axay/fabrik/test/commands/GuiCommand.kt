package net.axay.fabrik.test.commands

import net.axay.fabrik.core.text.literal
import net.axay.fabrik.igui.openGui
import net.axay.fabrik.test.igui.SimpleTestGui

private val guis = mapOf(
    "simpletestgui" to SimpleTestGui::create
)

val guiCommand = testCommand("gui") {
    literal("open") {
        argument<String>("guiname") { guiname ->
            suggestList { guis.keys }
            runs {
                val gui = guis[guiname()]
                if (gui != null)
                    source.player.openGui(gui(), 1)
                else source.sendError("This GUI does not exist!".literal)
            }
        }
    }
}
