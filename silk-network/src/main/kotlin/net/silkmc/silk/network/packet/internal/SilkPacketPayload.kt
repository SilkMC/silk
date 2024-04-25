package net.silkmc.silk.network.packet.internal

import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.silkmc.silk.core.annotations.InternalSilkApi
import net.silkmc.silk.network.packet.*

@InternalSilkApi
class SilkPacketPayload(
    @InternalSilkApi
    val definition: AbstractPacketDefinition<*, *>,
    val bytes: ByteArray,
) : CustomPacketPayload {

    override fun type(): CustomPacketPayload.Type<SilkPacketPayload> = definition.type

    fun onReceiveServer(context: ServerPacketContext) =
        when (definition) {
            is ClientToServerPacketDefinition -> { definition.onReceive(bytes, context); true }
            is ClientToClientPacketDefinition -> { definition.onReceiveServer(bytes, context); true }
            is ServerToClientPacketDefinition -> throw IllegalStateException("Cannot receive client-bound packet on server")
        }

    fun onReceiveClient(context: ClientPacketContext) =
        when (definition) {
            is ServerToClientPacketDefinition -> { definition.onReceive(bytes, context); true }
            is ClientToClientPacketDefinition -> { definition.onReceive(bytes, context); true }
            is ClientToServerPacketDefinition -> throw IllegalStateException("Cannot receive server-bound packet on client")
        }
}
