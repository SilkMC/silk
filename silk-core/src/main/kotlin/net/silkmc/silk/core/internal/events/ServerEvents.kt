package net.silkmc.silk.core.internal.events

import net.minecraft.server.MinecraftServer
import net.silkmc.silk.core.annotations.InternalSilkApi

@InternalSilkApi
class ServerEvents {
    class Init(val server: MinecraftServer) {
        companion object : Event<Init>()
    }
}
