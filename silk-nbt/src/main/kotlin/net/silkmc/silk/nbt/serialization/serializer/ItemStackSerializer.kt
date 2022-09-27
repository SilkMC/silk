package net.silkmc.silk.nbt.serialization.serializer

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.minecraft.nbt.CompoundTag
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.silkmc.silk.core.serialization.SilkSerializer

// TODO: Unit test
@ExperimentalSerializationApi
object ItemStackSerializer : SilkSerializer<ItemStack>() {
    override val descriptor = buildClassSerialDescriptor(descriptorName) {
        element<ResourceLocation>("id")
        element<Int>("Count")
        element<CompoundTag>("tag")
    }

    override fun deserialize(decoder: Decoder) =
        run { ItemStack.of(decoder.decodeSerializableValue(CompoundTagSerializer))!! }

    override fun serialize(encoder: Encoder, value: ItemStack) =
        encoder.encodeSerializableValue(CompoundTagSerializer, CompoundTag().also(value::save))
}