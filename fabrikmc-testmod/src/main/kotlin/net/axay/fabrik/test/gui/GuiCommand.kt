package net.axay.fabrik.test.gui

import com.mojang.brigadier.arguments.StringArgumentType
import net.axay.fabrik.commands.*
import net.axay.fabrik.igui.openGui

private val guis = mapOf(
    "simpletestgui" to SimpleTestGui.gui
)

val guiCommand = command("gui") {
    literal("open") {
        argument("guiname", StringArgumentType.string()) {
            simpleSuggests { guis.keys }
            simpleExecutes {
                it.source.player.openGui(guis[it.getArgument("guiname", String::class.java)]!!)
            }
        }
    }
}
