package net.silkmc.silk.network.internal

import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.resources.ResourceLocation
import net.silkmc.silk.network.packet.*

@PublishedApi
internal class SilkNetwork : ModInitializer, ClientModInitializer {
    companion object {
        val packetId = ResourceLocation("silk", "networking")
    }

    override fun onInitialize() {
        ServerPlayNetworking.registerGlobalReceiver(packetId) { server, player, handler, buf, responseSender ->
            val bytes = buf.readByteArray()
            val id = buf.readUtf()
            val context = ServerPacketContext(server, player, handler, responseSender)
            ClientToServerPacketDefinition.onReceive(bytes, id, context)
            ClientToClientPacketDefinition.onReceiveServer(bytes, id, context)
        }
    }

    override fun onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(packetId) { client, handler, buf, responseSender ->
            val bytes = buf.readByteArray()
            val id = buf.readUtf()
            val context = ClientPacketContext(client, handler, responseSender)
            ServerToClientPacketDefinition.onReceive(bytes, id, context)
            ClientToClientPacketDefinition.onReceive(bytes, id, context)
        }
    }
}
