package net.axay.silk.network.packet

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.cbor.Cbor
import net.axay.silk.core.Silk
import net.axay.silk.network.internal.SilkNetwork
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer

/**
 * See [s2cPacket] function, which constructs this packet definition class.
 */
class ServerToClientPacketDefinition<T : Any>(
    id: ResourceLocation,
    cbor: Cbor,
    deserializer: DeserializationStrategy<T>,
) : AbstractPacketDefinition<T, ClientPacketContext>(id, cbor, deserializer) {
    internal companion object : DefinitionRegistry<ClientPacketContext>()

    @PublishedApi
    internal fun push(buffer: FriendlyByteBuf, player: ServerPlayer) {
        ServerPlayNetworking.send(player, SilkNetwork.packetId, buffer)
    }

    /**
     * Sends the given [value] to the given [player]. This will result in the
     * serialization of the given value.
     */
    inline fun <reified TPacket : T> send(value: TPacket, player: ServerPlayer) =
        push(createBuffer(value), player)

    /**
     * Sends the given [value] to **all** players on the server. This will result the serialization
     * of the given value.
     */
    inline fun <reified TPacket : T> sendToAll(value: TPacket) {
        val buffer = createBuffer(value)
        Silk.currentServer?.playerList?.players?.forEach { push(buffer, it) }
    }

    /**
     * Executes the given [receiver] as a callback when this packet is received on the client-side.
     */
    fun receiveOnClient(receiver: suspend (packet: T, context: ClientPacketContext) -> Unit) {
        registerReceiver(receiver, Companion)
    }
}
