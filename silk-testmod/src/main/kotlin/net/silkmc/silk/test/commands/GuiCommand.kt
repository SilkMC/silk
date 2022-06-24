package net.silkmc.silk.test.commands

import net.silkmc.silk.core.text.literal
import net.silkmc.silk.igui.openGui
import net.silkmc.silk.test.igui.SimpleTestGui

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
