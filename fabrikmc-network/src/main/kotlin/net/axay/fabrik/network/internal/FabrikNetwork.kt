package net.axay.fabrik.network.internal

import net.axay.fabrik.network.packet.ClientPacketContext
import net.axay.fabrik.network.packet.ClientToServerPacketDefinition
import net.axay.fabrik.network.packet.ServerPacketContext
import net.axay.fabrik.network.packet.ServerToClientPacketDefinition
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.util.Identifier

internal class FabrikNetwork : ModInitializer, ClientModInitializer {
    companion object {
        val packetId = Identifier("fabrikmc", "networking")
    }

    override fun onInitialize() {
        ServerPlayNetworking.registerGlobalReceiver(packetId) { server, player, handler, buf, responseSender ->
            ClientToServerPacketDefinition.onReceive(
                buf.readByteArray(), buf.readString(),
                ServerPacketContext(server, player, handler, responseSender)
            )
        }
    }

    override fun onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(packetId) { client, handler, buf, responseSender ->
            ServerToClientPacketDefinition.onReceive(
                buf.readByteArray(), buf.readString(),
                ClientPacketContext(client, handler, responseSender)
            )
        }
    }
}
