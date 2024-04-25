package net.silkmc.silk.network.packet

import kotlinx.serialization.BinaryFormat
import kotlinx.serialization.KSerializer
import net.minecraft.client.Minecraft
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket
import net.minecraft.resources.ResourceLocation
import net.silkmc.silk.network.packet.internal.SilkPacketPayload

/**
 * See [c2sPacket] function, which constructs this packet definition class.
 */
class ClientToServerPacketDefinition<T : Any>(
    id: ResourceLocation,
    binaryFormat: BinaryFormat,
    deserializer: KSerializer<T>,
) : AbstractPacketDefinition<T, ServerPacketContext>(id, binaryFormat, deserializer) {

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
        registerReceiver(receiver)
    }

    private fun send(payload: SilkPacketPayload) {
        val connection = Minecraft.getInstance().connection ?: error("Cannot send packets to the server while not in-game")
        connection.send(ServerboundCustomPayloadPacket(payload))
    }

    internal companion object : DefinitionRegistry<ServerPacketContext>()
    init { register(this) }
}
