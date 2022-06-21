package net.silkmc.silk.core.internal.events

import net.minecraft.server.MinecraftServer
import net.silkmc.silk.core.annotations.InternalSilkApi

@InternalSilkApi
class ServerEvents {
    class Init(val server: MinecraftServer) {
        companion object {
            private val listeners = ArrayList<(Init) -> Unit>()
            fun register(callback: (Init) -> Unit) {
                listeners += callback
            }
            fun invoke(server: MinecraftServer) {
                val init = Init(server)
                listeners.forEach {
                    it(init)
                }
            }
        }
    }
}
