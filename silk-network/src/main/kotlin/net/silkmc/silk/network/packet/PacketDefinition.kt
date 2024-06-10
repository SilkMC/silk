@file:Suppress("MemberVisibilityCanBePrivate")

package net.silkmc.silk.network.packet

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.serialization.BinaryFormat
import kotlinx.serialization.KSerializer
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
import net.silkmc.silk.core.annotations.InternalSilkApi
import net.silkmc.silk.network.packet.internal.SilkPacketPayload
import java.util.concurrent.ConcurrentHashMap

/**
 * Abstraction of server-to-client and client-to-server packets. See [ServerToClientPacketDefinition]
 * and [ClientToServerPacketDefinition]. Additionally, this class is partially the basis of
 * [ClientToClientPacketDefinition].
 */
sealed class AbstractPacketDefinition<T : Any, C>(
    val id: ResourceLocation,
    val binaryFormat: BinaryFormat,
    private val serializer: KSerializer<T>,
) {
    private val registeredReceivers = ArrayList<suspend (T, C) -> Unit>()

    @OptIn(ExperimentalCoroutinesApi::class)
    protected val receiverScope = CoroutineScope(Dispatchers.Default.limitedParallelism(1))

    @InternalSilkApi
    val type: CustomPacketPayload.Type<SilkPacketPayload> = CustomPacketPayload.Type(id)

    @InternalSilkApi
    val streamCodec: StreamCodec<FriendlyByteBuf, SilkPacketPayload> =
        object : StreamCodec<FriendlyByteBuf, SilkPacketPayload> {
            override fun decode(buf: FriendlyByteBuf) = SilkPacketPayload(this@AbstractPacketDefinition, buf.readByteArray())
            override fun encode(buf: FriendlyByteBuf, payload: SilkPacketPayload) { buf.writeByteArray(payload.bytes) }
        }

    internal fun onReceive(bytes: ByteArray, context: C) {
        receiverScope.launch {
            if (registeredReceivers.isNotEmpty()) {
                val value = deserialize(bytes)
                for (receiver in registeredReceivers) {
                    receiver(value, context)
                }
            }
        }
    }

    internal fun registerReceiver(receiver: suspend (T, C) -> Unit) {
        receiverScope.launch {
            registeredReceivers += receiver
        }
    }

    internal fun deserialize(byteArray: ByteArray): T {
        return binaryFormat.decodeFromByteArray(serializer, byteArray)
    }

    protected fun createPayload(value: T): SilkPacketPayload {
        return SilkPacketPayload(
            this,
            binaryFormat.encodeToByteArray(serializer, value)
        )
    }

    internal open class DefinitionRegistry<C> {
        protected val registeredDefinitions = ConcurrentHashMap<ResourceLocation, AbstractPacketDefinition<*, C>>()

        fun register(definition: AbstractPacketDefinition<*, C>) {
            registeredDefinitions[definition.id] = definition
        }

        fun lookupDefinition(channel: ResourceLocation): AbstractPacketDefinition<*, C>? {
            return registeredDefinitions[channel]
        }
    }
}
