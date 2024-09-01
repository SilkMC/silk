package net.silkmc.silk.paper.internal

import net.silkmc.silk.core.annotations.InternalSilkApi
import net.silkmc.silk.core.event.*
import net.silkmc.silk.core.internal.SilkEntrypoint
import net.silkmc.silk.paper.conversions.mcServer
import net.silkmc.silk.paper.events.internal.setupPaper
import org.bukkit.plugin.java.JavaPlugin

class SilkPaperEntrypoint : JavaPlugin() {

    companion object {
        @InternalSilkApi
        lateinit var instance: SilkPaperEntrypoint
            private set
    }

    override fun onLoad() {
        instance = this
    }

    override fun onEnable() {
        SilkEntrypoint.onInit()

        Events.Server.preStart.invoke(ServerEvents.ServerEvent(server.mcServer))

        Events.Server.setupPaper()
        Events.Player.setupPaper()
        Events.Entity.setupPaper()
    }
}
