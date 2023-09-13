package net.silkmc.silk.network.packet

import kotlinx.serialization.KSerializer
import kotlinx.serialization.cbor.Cbor
import net.minecraft.client.Minecraft
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket
import net.minecraft.resources.ResourceLocation
import net.silkmc.silk.network.packet.internal.SilkPacketPayload

/**
 * See [c2sPacket] function, which constructs this packet definition class.
 */
class ClientToServerPacketDefinition<T : Any>(
    id: ResourceLocation,
    cbor: Cbor,
    deserializer: KSerializer<T>,
) : AbstractPacketDefinition<T, ServerPacketContext>(id, cbor, deserializer) {

    internal companion object : DefinitionRegistry<ServerPacketContext>()

    /**
     * Sends the given [value] as a packet to the server.
     */
    fun send(value: T) {
        send(createPayload(value))
    }

    /**
     * Executes the given [receiver] as a callback when this packet is received on the server-side.
     */
    fun receiveOnServer(receiver: suspend (packet: T, context: ServerPacketContext) -> Unit) {
        registerReceiver(receiver, Companion)
    }

    private fun send(payload: SilkPacketPayload) {
        val connection = Minecraft.getInstance().connection ?: error("Cannot send packets to the server while not in-game")
        connection.send(ServerboundCustomPayloadPacket(payload))
    }
}
