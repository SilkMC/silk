package net.silkmc.silk.test.commands

import net.silkmc.silk.core.text.literal
import net.silkmc.silk.igui.Gui
import net.silkmc.silk.igui.GuiType
import net.silkmc.silk.igui.openGui
import net.silkmc.silk.test.igui.OtherTestGui
import net.silkmc.silk.test.igui.SimpleTestGui

private val guis = mapOf<String, (GuiType) -> Gui>(
    "simpletestgui" to { SimpleTestGui.create() },
    "othertestgui" to { OtherTestGui.create(it) },
)

val guiCommand = testCommand("gui") {
    literal("open") {
        argument<String>("guiname") { guiname ->
            suggestList { guis.keys }
            argument<String>("guitype") { guitype ->
                suggestList { GuiType.entries.map(GuiType::name) }

                runs {
                    val createGui = guis[guiname()]
                    val type = GuiType.entries.find { it.name == guitype() }
                    if (createGui != null && type != null)
                        source.playerOrException.openGui(createGui(type), 1)
                    else source.sendFailure("This GUI or type does not exist!".literal)
                }
            }
        }
    }
}
