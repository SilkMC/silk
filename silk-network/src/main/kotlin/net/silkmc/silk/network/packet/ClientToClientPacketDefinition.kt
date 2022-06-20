@file:OptIn(ExperimentalSerializationApi::class)

package net.silkmc.silk.network.packet

import kotlinx.coroutines.launch
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.cbor.Cbor
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import net.silkmc.silk.network.internal.SilkNetwork

/**
 * Used by the [ClientToClientPacketDefinition], which requires a server-side forwarder.
 */
typealias ServerPacketForwarder<T> = suspend ClientToClientPacketDefinition<T>.(
    packet: ClientToClientPacketDefinition.SerializedPacket,
    context: ServerPacketContext,
) -> ServerPlayer?

/**
 * See [c2cPacket] function, which constructs this packet definition class.
 */
class ClientToClientPacketDefinition<T : Any>(
    id: ResourceLocation,
    cbor: Cbor,
    deserializer: DeserializationStrategy<T>,
) : AbstractPacketDefinition<T, ClientPacketContext>(id, cbor, deserializer) {
    internal companion object : DefinitionRegistry<ClientPacketContext>() {
        internal fun onReceiveServer(bytes: ByteArray, channel: String, context: ServerPacketContext) {
            packetCoroutineScope.launch {
                (definitionLock.read { registeredDefinitions[channel] } as ClientToClientPacketDefinition<*>?)
                    ?.onReceiveServer(bytes, context)
            }
        }
    }

    @JvmInline
    value class SerializedPacket(val bytes: ByteArray)

    private var forwarder: ServerPacketForwarder<T>? = null

    suspend fun onReceiveServer(bytes: ByteArray, context: ServerPacketContext) {
        val receiver = forwarder?.invoke(this, SerializedPacket(bytes), context)
        if (receiver != null) {
            val buffer = PacketByteBufs.create()
            buffer.writeByteArray(bytes)
            buffer.writeUtf(idString)
            ServerPlayNetworking.send(receiver, SilkNetwork.packetId, buffer)
        }
    }

    /**
     * Sends the given [value] as a packet to the server.
     */
    inline fun <reified TPacket : T> send(value: TPacket) =
        ClientPlayNetworking.send(SilkNetwork.packetId, createBuffer(value))

    /**
     * Specifies the forward logic (on the server, as it is the instance which forwards this packet).
     * The returned player will receive the packet. Return null if you do not wish to forward this packet.
     */
    fun forwardOnServer(forwarder: ServerPacketForwarder<T>) {
        packetCoroutineScope.launch {
            registerDefinition(this@ClientToClientPacketDefinition)
        }
        this.forwarder = forwarder
    }

    /**
     * Executes the given [receiver] as a callback when this packet is received on the client-side.
     */
    fun receiveOnClient(receiver: suspend (packet: T, context: ClientPacketContext) -> Unit) {
        registerReceiver(receiver, Companion)
    }

    /**
     * Deserializes this serialized packet to an instance of its original class.
     */
    fun SerializedPacket.deserialize(): T = deserialize(bytes)
}
