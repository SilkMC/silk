package net.axay.fabrik.network.packet

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.cbor.Cbor
import net.axay.fabrik.core.Fabrik
import net.axay.fabrik.network.internal.FabrikNetwork
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.network.PacketByteBuf
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier

/**
 * See [s2cPacket] function, which constructs this packet definition class.
 */
class ServerToClientPacketDefinition<T : Any>(
    id: Identifier,
    cbor: Cbor,
    deserializer: DeserializationStrategy<T>,
) : AbstractPacketDefinition<T, ClientPacketContext>(id, cbor, deserializer) {
    internal companion object : DefinitionRegistry<ClientPacketContext>()

    @PublishedApi
    internal fun push(buffer: PacketByteBuf, player: ServerPlayerEntity) {
        ServerPlayNetworking.send(player, FabrikNetwork.packetId, buffer)
    }

    /**
     * Sends the given [value] to the given [player]. This will result in the
     * serialization of the given value.
     */
    inline fun <reified TPacket : T> send(value: TPacket, player: ServerPlayerEntity) =
        push(createBuffer(value), player)

    /**
     * Sends the given [value] to **all** players on the server. This will result the serialization
     * of the given value.
     */
    inline fun <reified TPacket : T> sendToAll(value: TPacket) {
        val buffer = createBuffer(value)
        Fabrik.currentServer?.playerManager?.playerList?.forEach { push(buffer, it) }
    }

    /**
     * Executes the given [receiver] as a callback when this packet is received on the client-side.
     */
    fun receiveOnClient(receiver: suspend (packet: T, context: ClientPacketContext) -> Unit) {
        registerReceiver(receiver, Companion)
    }
}
