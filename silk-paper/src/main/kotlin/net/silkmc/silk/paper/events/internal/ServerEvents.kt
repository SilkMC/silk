package net.silkmc.silk.paper.events.internal

import net.silkmc.silk.core.event.ServerEvents
import net.silkmc.silk.paper.conversions.mcServer
import net.silkmc.silk.paper.events.listenSilk
import net.silkmc.silk.paper.internal.SilkPaperEntrypoint
import org.bukkit.event.server.PluginDisableEvent
import org.bukkit.event.server.ServerLoadEvent

fun ServerEvents.setupPaper() {
    listenSilk<ServerLoadEvent> {
        postStart.invoke(ServerEvents.ServerEvent(SilkPaperEntrypoint.instance.server.mcServer))
    }
    listenSilk<PluginDisableEvent> {
        if (it.plugin == SilkPaperEntrypoint.instance) {
            preStop.invoke(ServerEvents.ServerEvent(SilkPaperEntrypoint.instance.server.mcServer))
        }
    }
}
