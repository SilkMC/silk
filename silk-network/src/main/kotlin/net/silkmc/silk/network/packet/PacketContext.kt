package net.silkmc.silk.network.packet

import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.minecraft.client.Minecraft
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ServerGamePacketListener
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer

class ServerPacketContext(
    val server: MinecraftServer,
    val player: ServerPlayer,
    val handler: ServerGamePacketListener,
    val responseSender: PacketSender,
)

class ClientPacketContext(
    val client: Minecraft,
    val handler: ClientGamePacketListener,
    val responseSender: PacketSender,
)
