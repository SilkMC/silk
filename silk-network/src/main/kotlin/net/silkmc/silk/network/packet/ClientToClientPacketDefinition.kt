@file:OptIn(ExperimentalSerializationApi::class)
@file:Suppress("MemberVisibilityCanBePrivate")

package net.silkmc.silk.network.packet

import io.netty.buffer.Unpooled
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.cbor.Cbor
import net.minecraft.client.Minecraft
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerPlayer

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
    cbor: Cbor,
    deserializer: KSerializer<T>,
) : AbstractPacketDefinition<T, ClientPacketContext>(id, cbor, deserializer) {

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
    private fun onReceiveServer(bytes: ByteArray, context: ServerPacketContext) {
        receiverScope.launch {
            val receivers = forwarder?.invoke(this@ClientToClientPacketDefinition, SerializedPacket(bytes), context)
            if (receivers?.isNotEmpty() == true) {
                val buffer = FriendlyByteBuf(Unpooled.buffer())
                buffer.writeByteArray(bytes)
                receivers.forEach { it.connection.send(ClientboundCustomPayloadPacket(id, buffer)) }
            }
        }
    }

    /**
     * Sends the given [value] as a packet to the server, which will then
     * forward it to players selected by the forwarder. You can specify the
     * forwarder using [forwardOnServer].
     */
    fun send(value: T) {
        send(createBuffer(value))
    }

    /**
     * Specifies the forward logic (on the server, as it is the instance which forwards this packet).
     * The returned player will receive the packet. Return null if you do not wish to forward this packet.
     */
    fun forwardOnServer(forwarder: ServerPacketForwarder<T>) {
        receiverScope.launch {
            register(this@ClientToClientPacketDefinition)
            this@ClientToClientPacketDefinition.forwarder = forwarder
        }
    }

    /**
     * Executes the given [receiver] as a callback when this packet is received on the client-side.
     */
    fun receiveOnClient(receiver: suspend (packet: T, context: ClientPacketContext) -> Unit) {
        registerReceiver(receiver, Companion)
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

    private fun send(buffer: FriendlyByteBuf) {
        val connection = Minecraft.getInstance().connection ?: error("Cannot send packets to the server while not in-game")
        connection.send(ServerboundCustomPayloadPacket(id, buffer))
    }

    internal companion object : DefinitionRegistry<ClientPacketContext>() {
        /**
         * @see [ClientToClientPacketDefinition.onReceiveServer]
         */
        fun onReceiveServer(channel: ResourceLocation, bytes: ByteArray, context: ServerPacketContext): Boolean {
            (registeredDefinitions[channel] as? ClientToClientPacketDefinition<*>?)
                ?.onReceiveServer(bytes, context) ?: return false
            return true
        }
    }
}
