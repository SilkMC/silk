package net.axay.fabrik.network.packet

import net.fabricmc.fabric.api.networking.v1.PacketSender
import net.minecraft.client.MinecraftClient
import net.minecraft.client.network.ClientPlayNetworkHandler
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayNetworkHandler
import net.minecraft.server.network.ServerPlayerEntity

class ServerPacketContext(
    val server: MinecraftServer,
    val player: ServerPlayerEntity,
    val handler: ServerPlayNetworkHandler,
    val responseSender: PacketSender,
)

class ClientPacketContext(
    val client: MinecraftClient,
    val handler: ClientPlayNetworkHandler,
    val responseSender: PacketSender,
)
