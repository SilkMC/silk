package net.silkmc.silk.network.packet

import net.minecraft.client.Minecraft
import net.minecraft.client.multiplayer.ClientPacketListener
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ServerGamePacketListener
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.network.ServerGamePacketListenerImpl

class ServerPacketContext(
    val server: MinecraftServer,
    val player: ServerPlayer,
    val handler: ServerGamePacketListener,
) {
    constructor(handler: ServerGamePacketListenerImpl) : this(
        handler.getPlayer().getServer() ?: error("ServerPlayer.getServer() is not allowed to be null"),
        handler.getPlayer(),
        handler,
    )
}

class ClientPacketContext(
    val client: Minecraft,
    val handler: ClientGamePacketListener,
) {
    constructor(handler: ClientPacketListener) : this(
        Minecraft.getInstance(),
        handler,
    )
}
