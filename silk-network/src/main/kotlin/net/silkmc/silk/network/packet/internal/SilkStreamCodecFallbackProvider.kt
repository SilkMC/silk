package net.silkmc.silk.network.packet.internal

import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.network.protocol.common.custom.CustomPacketPayload.FallbackProvider
import net.minecraft.resources.Identifier
import net.silkmc.silk.core.logging.logWarning
import net.silkmc.silk.network.packet.AbstractPacketDefinition.DefinitionRegistry
import net.silkmc.silk.network.packet.ClientToClientPacketDefinition

internal class SilkStreamCodecFallbackProvider(
    private val directionalDefinitionRegistry: DefinitionRegistry<*>,
    private val standardFallbackProvider: FallbackProvider<FriendlyByteBuf>,
    private val directionName: String,
): FallbackProvider<FriendlyByteBuf> {

    override fun create(identifier: Identifier): StreamCodec<FriendlyByteBuf, out CustomPacketPayload> {
        val directionalDefinition = directionalDefinitionRegistry.lookupDefinition(identifier)
        val clientToClientDefinition = ClientToClientPacketDefinition.lookupDefinition(identifier)
        // ^ client-to-client is always relevant, because the server will forward it to the clients

        if (directionalDefinition != null && clientToClientDefinition != null) {
            logWarning("Packet with id '${identifier}' is registered both as $directionName and client-to-client packet")
        }

        return directionalDefinition?.streamCodec
            ?: (clientToClientDefinition?.streamCodec
                ?: standardFallbackProvider.create(identifier))
    }
}
