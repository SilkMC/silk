package net.axay.silk.core.packet

import net.minecraft.network.protocol.Packet
import net.minecraft.server.level.ServerPlayer

/**
 * Sends the packet to all players in the iterable.
 */
fun Iterable<ServerPlayer>.sendPacket(packet: Packet<*>) {
    forEach { it.connection.send(packet) }
}
