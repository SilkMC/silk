@file:OptIn(ExperimentalSerializationApi::class)

package net.axay.silk.network.packet

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.cbor.Cbor
import kotlinx.serialization.encodeToByteArray
import kotlinx.serialization.serializer
import net.axay.silk.core.kotlin.ReadWriteMutex
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation

/**
 * Creates a new [ServerToClientPacketDefinition]. This packet can only be sent
 * from the server to one or multiple clients. The packet can only be sent
 * in a typesafe way. The type is specified by [T].
 *
 * @param id the [ResourceLocation] allowing communication between server and client as they
 * both know this identifier
 * @param cbor (optional) the [Cbor] instanced used for serialization and deserialization of this packet
 */
inline fun <reified T : Any> s2cPacket(id: ResourceLocation, cbor: Cbor = Cbor) =
    ServerToClientPacketDefinition<T>(id, cbor, cbor.serializersModule.serializer())

/**
 * Creates a new [ClientToServerPacketDefinition]. This packet can only be sent
 * from the client to the current server. The packet can only be sent
 * in a typesafe way. The type is specified by [T].
 *
 * @param id the [ResourceLocation] allowing communication between server and client as they
 * both know this identifier
 * @param cbor (optional) the [Cbor] instanced used for serialization and deserialization of this packet
 */
inline fun <reified T : Any> c2sPacket(id: ResourceLocation, cbor: Cbor = Cbor) =
    ClientToServerPacketDefinition<T>(id, cbor, cbor.serializersModule.serializer())

/**
 * Creates a new [ClientToClientPacketDefinition]. This packet can only be sent
 * from the client to another client. The server will act as the middle man, it is
 * responsible for forwarding this packet. The packet can only be sent
 * in a typesafe way. The type is specified by [T].
 *
 * @param id the [ResourceLocation] allowing communication between server and client as they
 * both know this identifier
 * @param cbor (optional) the [Cbor] instanced used for serialization and deserialization of this packet
 */
inline fun <reified T : Any> c2cPacket(id: ResourceLocation, cbor: Cbor = Cbor) =
    ClientToClientPacketDefinition<T>(id, cbor, cbor.serializersModule.serializer())

/**
 * Abstraction of server-to-client and client-to-server packets. See [ServerToClientPacketDefinition]
 * and [ClientToServerPacketDefinition].
 */
abstract class AbstractPacketDefinition<T : Any, C> internal constructor(
    id: ResourceLocation,
    val cbor: Cbor,
    private val deserializer: DeserializationStrategy<T>,
) {
    companion object {
        /**
         * The [CoroutineScope] used for packet callback handling.
         */
        val packetCoroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    }

    internal open class DefinitionRegistry<C> {
        protected val registeredDefinitions = HashMap<String, AbstractPacketDefinition<*, C>>()

        protected val definitionLock = ReadWriteMutex()

        internal suspend fun registerDefinition(definition: AbstractPacketDefinition<*, C>) {
            definitionLock.write {
                registeredDefinitions[definition.idString] = definition
            }
        }

        internal fun onReceive(bytes: ByteArray, channel: String, context: C) {
            packetCoroutineScope.launch {
                definitionLock.read { registeredDefinitions[channel] }?.onReceive(bytes, context)
            }
        }
    }

    /**
     * The stored [ResourceLocation] of this packet in its string representation.
     */
    val idString = id.toString()

    private val registeredReceivers = ArrayList<suspend (T, C) -> Unit>()

    private val receiverLock = ReadWriteMutex()

    private suspend fun onReceive(bytes: ByteArray, context: C) {
        receiverLock.read {
            if (registeredReceivers.isNotEmpty()) {
                val value = deserialize(bytes)
                for (receiver in registeredReceivers) {
                    receiver(value, context)
                }
            }
        }
    }

    internal fun registerReceiver(receiver: suspend (T, C) -> Unit, definitionRegistry: DefinitionRegistry<C>) {
        packetCoroutineScope.launch {
            definitionRegistry.registerDefinition(this@AbstractPacketDefinition)
            receiverLock.write {
                registeredReceivers += receiver
            }
        }
    }

    internal fun deserialize(byteArray: ByteArray): T {
        return cbor.decodeFromByteArray(deserializer, byteArray)
    }

    @PublishedApi
    internal inline fun <reified TPacket : T> createBuffer(value: TPacket): FriendlyByteBuf {
        val buffer = PacketByteBufs.create()
        buffer.writeByteArray(cbor.encodeToByteArray(value))
        buffer.writeUtf(idString)
        return buffer
    }
}
