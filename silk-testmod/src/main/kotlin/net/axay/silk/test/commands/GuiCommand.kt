package net.axay.silk.test.commands

import net.axay.silk.core.text.literal
import net.axay.silk.igui.openGui
import net.axay.silk.test.igui.SimpleTestGui

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
                    source.playerOrException.openGui(gui(), 1)
                else source.sendFailure("This GUI does not exist!".literal)
            }
        }
    }
}
