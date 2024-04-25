package net.silkmc.silk.network.packet

import kotlinx.serialization.BinaryFormat
import kotlinx.serialization.KSerializer
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import net.silkmc.silk.core.Silk
import net.silkmc.silk.network.packet.internal.SilkPacketPayload

/**
 * See [s2cPacket] function, which constructs this packet definition class.
 */
class ServerToClientPacketDefinition<T : Any>(
    id: ResourceLocation,
    binaryFormat: BinaryFormat,
    deserializer: KSerializer<T>,
) : AbstractPacketDefinition<T, ClientPacketContext>(id, binaryFormat, deserializer) {

    /**
     * Sends the given [value] to the given [player]. This will result in the
     * serialization of the given value.
     */
    fun send(value: T, player: ServerPlayer) {
        send(createPayload(value), player)
    }

    /**
     * Sends the given [value] to **all** players on the server. This will result the serialization
     * of the given value.
     */
    fun sendToAll(value: T) {
        val buffer = createPayload(value)
        Silk.players.forEach { send(buffer, it) }
    }

    /**
     * Executes the given [receiver] as a callback when this packet is received on the client-side.
     */
    fun receiveOnClient(receiver: suspend (packet: T, context: ClientPacketContext) -> Unit) {
        registerReceiver(receiver)
    }

    private fun send(payload: SilkPacketPayload, player: ServerPlayer) {
        player.connection.send(ClientboundCustomPayloadPacket(payload))
    }

    internal companion object : DefinitionRegistry<ClientPacketContext>()
    init { register(this) }
}
