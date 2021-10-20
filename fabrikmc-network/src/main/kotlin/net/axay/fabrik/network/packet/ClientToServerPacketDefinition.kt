package net.axay.fabrik.network.packet

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.cbor.Cbor
import net.axay.fabrik.network.internal.FabrikNetwork
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.Identifier

/**
 * See [c2sPacket] function, which constructs this packet definition class.
 */
class ClientToServerPacketDefinition<T : Any>(
    id: Identifier,
    cbor: Cbor,
    deserializer: DeserializationStrategy<T>,
) : AbstractPacketDefinition<T, ServerPacketContext>(id, cbor, deserializer) {
    internal companion object : DefinitionRegistry<ServerPacketContext>()

    @PublishedApi
    internal fun push(buffer: PacketByteBuf) {
        ClientPlayNetworking.send(FabrikNetwork.packetId, buffer)
    }

    /**
     * Sends the given [value] as a packet to the server.
     */
    inline fun <reified TPacket : T> send(value: TPacket) = push(createBuffer(value))

    /**
     * Executes the given [receiver] as a callback when this packet is received on the server-side.
     */
    fun receiveOnServer(receiver: suspend (packet: T, context: ServerPacketContext) -> Unit) {
        registerReceiver(receiver, Companion)
    }
}
