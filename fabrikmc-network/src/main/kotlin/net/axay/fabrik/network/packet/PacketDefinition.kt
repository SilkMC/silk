@file:OptIn(ExperimentalSerializationApi::class)

package net.axay.fabrik.network.packet

import io.netty.buffer.Unpooled
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.serializer
import net.axay.fabrik.core.Fabrik
import net.axay.fabrik.core.kotlin.ReadWriteMutex
import net.axay.fabrik.network.internal.FabrikNetwork
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.network.PacketByteBuf
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier

/**
 * Creates a new [ServerToClientPacketDefinition]. This packet can only be sent
 * from the server to one or multiple clients. The packet can only be sent
 * in a typesafe way. The type is specified by [T].
 *
 * @param id the [Identifier] allowing communication between server and client as they
 * both know this identifier
 * @param cbor (optional) the [Cbor] instanced used for serialization and deserialization of this packet
 */
inline fun <reified T : Any> s2cPacket(id: Identifier, cbor: Cbor = Cbor) =
    ServerToClientPacketDefinition<T>(id, cbor, cbor.serializersModule.serializer())

class ServerToClientPacketDefinition<T : Any>(
    id: Identifier,
    cbor: Cbor,
    deserializer: DeserializationStrategy<T>,
) : AbstractPacketDefinition<T>(id, cbor, deserializer) {
    companion object {
        internal val registeredDefinitions = HashMap<String, AbstractPacketDefinition<*>>()
    }

    @PublishedApi
    internal fun push(buffer: PacketByteBuf, player: ServerPlayerEntity) {
        ServerPlayNetworking.send(player, FabrikNetwork.packetId, buffer)
    }

    /**
     * Sends the given [value] to the given [player]. This will result in the
     * serialization of the given value.
     */
    inline fun <reified T> send(value: T, player: ServerPlayerEntity) =
        push(createBuffer(value), player)

    /**
     * Sends the given [value] to **all** players on the server. This will result the serialization
     * of the given value.
     */
    inline fun <reified T> sendToAll(value: T) {
        val buffer = createBuffer(value)
        Fabrik.currentServer?.playerManager?.playerList?.forEach { push(buffer, it) }
    }

    /**
     * Executes the given [receiver] as a callback when this packet is received.
     */
    fun receiveOnClient(receiver: suspend (T) -> Unit) {
        registerReceiver(receiver, registeredDefinitions)
    }
}

abstract class AbstractPacketDefinition<T : Any> internal constructor(
    id: Identifier,
    val cbor: Cbor,
    val deserializer: DeserializationStrategy<T>,
) {
    companion object {
        /**
         * The [CoroutineScope] used for packet callback handling.
         */
        val packetCoroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    }

    val idString = id.toString()

    internal val registeredReceivers = ArrayList<suspend (T) -> Unit>()

    private val receiverLock = ReadWriteMutex()

    internal fun onReceive(bytes: ByteArray) {
        packetCoroutineScope.launch {
            receiverLock.read {
                if (registeredReceivers.isNotEmpty()) {
                    val value = deserialize(bytes)
                    registeredReceivers.forEach { receiver ->
                        receiver(value)
                    }
                }
            }
        }
    }

    protected fun registerReceiver(
        receiver: suspend (T) -> Unit,
        definitionRegistry: MutableMap<String, AbstractPacketDefinition<*>>,
    ) {
        packetCoroutineScope.launch {
            receiverLock.write {
                if (!definitionRegistry.containsKey(idString))
                    definitionRegistry[idString] = this@AbstractPacketDefinition
                registeredReceivers += receiver
            }
        }
    }

    protected fun deserialize(byteArray: ByteArray): T {
        return cbor.decodeFromByteArray(deserializer, byteArray)
    }

    @PublishedApi
    internal inline fun <reified T> createBuffer(value: T): PacketByteBuf {
        val buffer = PacketByteBuf(Unpooled.wrappedBuffer(cbor.encodeToByteArray(value)))
        buffer.writeString(idString)
        return buffer
    }
}
