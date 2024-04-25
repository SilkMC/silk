@file:Suppress("MemberVisibilityCanBePrivate")

package net.silkmc.silk.network.packet

import kotlinx.coroutines.launch
import kotlinx.serialization.BinaryFormat
import kotlinx.serialization.KSerializer
import net.minecraft.client.Minecraft
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket
import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer
import net.silkmc.silk.network.packet.internal.SilkPacketPayload

/**
 * Used by the [ClientToClientPacketDefinition], which requires a server-side forwarder.
 */
typealias ServerPacketForwarder<T> = suspend ClientToClientPacketDefinition<T>.(
    packet: ClientToClientPacketDefinition.SerializedPacket,
    context: ServerPacketContext,
) -> Collection<ServerPlayer>

/**
 * See [c2cPacket] function, which constructs this packet definition class.
 */
class ClientToClientPacketDefinition<T : Any>(
    id: ResourceLocation,
    binaryFormat: BinaryFormat,
    deserializer: KSerializer<T>,
) : AbstractPacketDefinition<T, ClientPacketContext>(id, binaryFormat, deserializer) {

    /**
     * The forwarder responsible for deciding which packets will be forwarded
     * to which players.
     *
     * @see ServerPacketForwarder
     * @see forwardOnServer
     */
    private var forwarder: ServerPacketForwarder<T>? = null

    /**
     * A special receive function, which handles incoming packets on the server,
     * which are meant to be forwarded to another client.
     */
    internal fun onReceiveServer(bytes: ByteArray, context: ServerPacketContext) {
        receiverScope.launch {
            val receivers = forwarder?.invoke(this@ClientToClientPacketDefinition, SerializedPacket(bytes), context)
            if (receivers?.isNotEmpty() == true) {
                val payload = SilkPacketPayload(this@ClientToClientPacketDefinition, bytes)
                receivers.forEach { it.connection.send(ClientboundCustomPayloadPacket(payload)) }
            }
        }
    }

    /**
     * Sends the given [value] as a packet to the server, which will then
     * forward it to players selected by the forwarder. You can specify the
     * forwarder using [forwardOnServer].
     */
    fun send(value: T) {
        send(createPayload(value))
    }

    /**
     * Specifies the forward logic (on the server, as it is the instance which forwards this packet).
     * The returned player will receive the packet. Return null if you do not wish to forward this packet.
     */
    fun forwardOnServer(forwarder: ServerPacketForwarder<T>) {
        receiverScope.launch {
            this@ClientToClientPacketDefinition.forwarder = forwarder
        }
    }

    /**
     * Executes the given [receiver] as a callback when this packet is received on the client-side.
     */
    fun receiveOnClient(receiver: suspend (packet: T, context: ClientPacketContext) -> Unit) {
        registerReceiver(receiver)
    }

    /**
     * A wrapper around a byte array containing packet data.
     * This wrapper will be used when forwarding packets on the server,
     * since deserialization should only happen when requested.
     */
    @JvmInline
    value class SerializedPacket(val bytes: ByteArray)

    /**
     * Deserializes this serialized packet to an instance of its original class.
     */
    fun SerializedPacket.deserialize(): T = deserialize(bytes)

    private fun send(payload: SilkPacketPayload) {
        val connection = Minecraft.getInstance().connection ?: error("Cannot send packets to the server while not in-game")
        connection.send(ServerboundCustomPayloadPacket(payload))
    }

    internal companion object : DefinitionRegistry<ClientPacketContext>()
    init { register(this) }
}
