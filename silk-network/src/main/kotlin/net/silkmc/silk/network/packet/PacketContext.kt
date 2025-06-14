package net.silkmc.silk.network.packet

import net.minecraft.client.Minecraft
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.network.ServerGamePacketListenerImpl

class ServerPacketContext(
    val server: MinecraftServer,
    val player: ServerPlayer,
    val handler: ServerGamePacketListenerImpl,
) {
    constructor(handler: ServerGamePacketListenerImpl) : this(
        handler.getPlayer().level().server,
        handler.getPlayer(),
        handler,
    )
}

class ClientPacketContext(
    val client: Minecraft = Minecraft.getInstance()
)
