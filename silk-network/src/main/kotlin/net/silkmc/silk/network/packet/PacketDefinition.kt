@file:OptIn(ExperimentalSerializationApi::class)
@file:Suppress("MemberVisibilityCanBePrivate")

package net.silkmc.silk.network.packet

import io.netty.buffer.Unpooled
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.cbor.Cbor
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.resources.ResourceLocation
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

    protected fun createBuffer(value: T): FriendlyByteBuf {
        val buffer = FriendlyByteBuf(Unpooled.buffer())
        buffer.writeByteArray(cbor.encodeToByteArray(serializer, value))
        return buffer
    }

    internal open class DefinitionRegistry<C> {
        protected val registeredDefinitions = ConcurrentHashMap<ResourceLocation, AbstractPacketDefinition<*, C>>()

        fun register(definition: AbstractPacketDefinition<*, C>) {
            registeredDefinitions[definition.id] = definition
        }

        fun onReceive(channel: ResourceLocation, byteBuf: FriendlyByteBuf, context: C): Boolean {
            registeredDefinitions[channel]?.onReceive(byteBuf.readByteArray(), context) ?: return false
            return true
        }
    }
}
