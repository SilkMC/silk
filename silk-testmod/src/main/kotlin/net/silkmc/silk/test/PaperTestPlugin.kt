package net.silkmc.silk.test

import org.bukkit.plugin.java.JavaPlugin

class PaperTestPlugin : JavaPlugin() {
    override fun onEnable() = SilkTest.initServer()
}
