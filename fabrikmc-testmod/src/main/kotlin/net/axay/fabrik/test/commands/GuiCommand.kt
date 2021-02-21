package net.axay.fabrik.test.commands

import com.mojang.brigadier.arguments.StringArgumentType
import net.axay.fabrik.commands.*
import net.axay.fabrik.igui.openGui
import net.axay.fabrik.test.gui.SimpleTestGui
import net.minecraft.text.LiteralText

private val guis = mapOf(
    "simpletestgui" to SimpleTestGui.gui
)

val guiCommand = command("gui") {
    literal("open") {
        argument("guiname", StringArgumentType.string()) {
            simpleSuggests { guis.keys }
            simpleExecutes {
                val gui = guis[it.getArgument("guiname", String::class.java)]
                if (gui != null)
                    it.source.player.openGui(gui)
                else it.source.sendError(LiteralText("Dieses GUI existiert nicht"))
            }
        }
    }
}
