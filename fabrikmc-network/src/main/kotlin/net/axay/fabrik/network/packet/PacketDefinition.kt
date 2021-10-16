@file:OptIn(ExperimentalSerializationApi::class)

package net.axay.fabrik.network.packet

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
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
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

/**
 * Creates a new [ClientToServerPacketDefinition]. This packet can only be sent
 * from the client to the current server. The packet can only be sent
 * in a typesafe way. The type is specified by [T].
 *
 * @param id the [Identifier] allowing communication between server and client as they
 * both know this identifier
 * @param cbor (optional) the [Cbor] instanced used for serialization and deserialization of this packet
 */
inline fun <reified T : Any> c2sPacket(id: Identifier, cbor: Cbor = Cbor) =
    ClientToServerPacketDefinition<T>(id, cbor, cbor.serializersModule.serializer())

/**
 * See [s2cPacket] function, which constructs this packet definition class.
 */
class ServerToClientPacketDefinition<T : Any>(
    id: Identifier,
    cbor: Cbor,
    deserializer: DeserializationStrategy<T>,
) : AbstractPacketDefinition<T>(id, cbor, deserializer) {
    private companion object : DefinitionRegistry()

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
    fun receiveOnClient(receiver: suspend (T) -> Unit) {
        registerReceiver(receiver, Companion)
    }
}

/**
 * See [c2sPacket] function, which constructs this packet definition class.
 */
class ClientToServerPacketDefinition<T : Any>(
    id: Identifier,
    cbor: Cbor,
    deserializer: DeserializationStrategy<T>,
) : AbstractPacketDefinition<T>(id, cbor, deserializer) {
    private companion object : DefinitionRegistry()

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
    fun receiveOnServer(receiver: suspend (T) -> Unit) {
        registerReceiver(receiver, Companion)
    }
}

/**
 * Abstraction of server-to-client and client-to-server packets. See [ServerToClientPacketDefinition]
 * and [ClientToServerPacketDefinition].
 */
abstract class AbstractPacketDefinition<T : Any> internal constructor(
    id: Identifier,
    val cbor: Cbor,
    private val deserializer: DeserializationStrategy<T>,
) {
    companion object {
        /**
         * The [CoroutineScope] used for packet callback handling.
         */
        val packetCoroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    }

    protected open class DefinitionRegistry {
        private val registeredDefinitions = HashMap<String, AbstractPacketDefinition<*>>()

        private val definitionLock = ReadWriteMutex()

        internal suspend fun registerDefinition(definition: AbstractPacketDefinition<*>) {
            definitionLock.write {
                registeredDefinitions[definition.idString] = definition
            }
        }

        internal fun onReceive(bytes: ByteArray, channel: String) {
            packetCoroutineScope.launch {
                definitionLock.read { registeredDefinitions[channel] }?.onReceive(bytes)
            }
        }
    }

    /**
     * The stored [Identifier] of this packet in its string representation.
     */
    val idString = id.toString()

    private val registeredReceivers = ArrayList<suspend (T) -> Unit>()

    private val receiverLock = ReadWriteMutex()

    private suspend fun onReceive(bytes: ByteArray) {
        receiverLock.read {
            if (registeredReceivers.isNotEmpty()) {
                val value = deserialize(bytes)
                registeredReceivers.forEach { receiver ->
                    receiver(value)
                }
            }
        }
    }

    protected fun registerReceiver(receiver: suspend (T) -> Unit, definitionRegistry: DefinitionRegistry) {
        packetCoroutineScope.launch {
            definitionRegistry.registerDefinition(this@AbstractPacketDefinition)
            receiverLock.write {
                registeredReceivers += receiver
            }
        }
    }

    private fun deserialize(byteArray: ByteArray): T {
        return cbor.decodeFromByteArray(deserializer, byteArray)
    }

    @PublishedApi
    internal inline fun <reified TPacket : T> createBuffer(value: TPacket): PacketByteBuf {
        val buffer = PacketByteBufs.create()
        buffer.writeByteArray(cbor.encodeToByteArray(value))
        buffer.writeString(idString)
        return buffer
    }
}
