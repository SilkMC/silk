@file:OptIn(ExperimentalSerializationApi::class)
@file:Suppress("MemberVisibilityCanBePrivate")

package net.silkmc.silk.network.packet

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.cbor.Cbor
import net.minecraft.resources.ResourceLocation
import net.silkmc.silk.network.packet.internal.SilkPacketPayload
import java.util.concurrent.ConcurrentHashMap

/**
 * Abstraction of server-to-client and client-to-server packets. See [ServerToClientPacketDefinition]
 * and [ClientToServerPacketDefinition]. Additionally, this class is partially the basis of
 * [ClientToClientPacketDefinition].
 */
abstract class AbstractPacketDefinition<T : Any, C> internal constructor(
    val id: ResourceLocation,
    val cbor: Cbor,
    private val serializer: KSerializer<T>,
) {
    private val registeredReceivers = ArrayList<suspend (T, C) -> Unit>()

    @OptIn(ExperimentalCoroutinesApi::class)
    protected val receiverScope = CoroutineScope(Dispatchers.Default.limitedParallelism(1))

//    @InternalSilkApi
//    val type: CustomPacketPayload.Type<SilkPacketPayload> = CustomPacketPayload.createType(id.toString())

    private fun onReceive(bytes: ByteArray, context: C) {
        receiverScope.launch {
            if (registeredReceivers.isNotEmpty()) {
                val value = deserialize(bytes)
                for (receiver in registeredReceivers) {
                    receiver(value, context)
                }
            }
        }
    }

    internal fun registerReceiver(receiver: suspend (T, C) -> Unit, definitionRegistry: DefinitionRegistry<C>) {
        receiverScope.launch {
            definitionRegistry.register(this@AbstractPacketDefinition)
            registeredReceivers += receiver
        }
    }

    internal fun deserialize(byteArray: ByteArray): T {
        return cbor.decodeFromByteArray(serializer, byteArray)
    }

    protected fun createPayload(value: T): SilkPacketPayload {
        return SilkPacketPayload(
            id,
            cbor.encodeToByteArray(serializer, value)
        )
    }

    internal open class DefinitionRegistry<C> {
        protected val registeredDefinitions = ConcurrentHashMap<ResourceLocation, AbstractPacketDefinition<*, C>>()

        fun register(definition: AbstractPacketDefinition<*, C>) {
            registeredDefinitions[definition.id] = definition
        }

        fun onReceive(channel: ResourceLocation, bytes: ByteArray, context: C): Boolean {
            registeredDefinitions[channel]?.onReceive(bytes, context) ?: return false
            return true
        }

        fun isRegistered(channel: ResourceLocation): Boolean {
            return registeredDefinitions.containsKey(channel)
        }
    }
}
