package net.silkmc.silk.paper.internal

import net.silkmc.silk.core.annotations.InternalSilkApi
import net.silkmc.silk.core.event.Entity
import net.silkmc.silk.core.event.Events
import net.silkmc.silk.core.event.Player
import net.silkmc.silk.core.event.Server
import net.silkmc.silk.paper.events.internal.setupPaper
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

class SilkPaperEntrypoint : JavaPlugin(), Listener {

    companion object {
        @InternalSilkApi
        lateinit var instance: SilkPaperEntrypoint
            private set
    }

    override fun onLoad() {
        instance = this

        Events.Server.setupPaper()
        Events.Player.setupPaper()
        Events.Entity.setupPaper()
    }

    override fun onEnable() {

    }
}
