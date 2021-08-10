package net.axay.fabrik.core.packet

import net.minecraft.network.Packet
import net.minecraft.server.network.ServerPlayerEntity

/**
 * Sends the packet to all players in the iterable.
 */
fun Iterable<ServerPlayerEntity>.sendPacket(packet: Packet<*>) {
    forEach { it.networkHandler.sendPacket(packet) }
}
