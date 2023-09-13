package net.silkmc.silk.network.packet.internal

import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
import net.silkmc.silk.core.annotations.InternalSilkApi

@InternalSilkApi
class SilkPacketPayload(
    private val id: ResourceLocation,
    val bytes: ByteArray,
) : CustomPacketPayload {

    override fun id() = id

    override fun write(friendlyByteBuf: FriendlyByteBuf) {
        friendlyByteBuf.writeByteArray(bytes)
    }
}
