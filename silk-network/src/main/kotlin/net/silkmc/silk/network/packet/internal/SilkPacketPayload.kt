package net.silkmc.silk.network.packet.internal

import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
import net.silkmc.silk.core.annotations.InternalSilkApi

@InternalSilkApi
class SilkPacketPayload(
    id: ResourceLocation,
    val bytes: ByteArray,
) : CustomPacketPayload {
    private val type = CustomPacketPayload.Type<SilkPacketPayload>(id)

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = type
}
