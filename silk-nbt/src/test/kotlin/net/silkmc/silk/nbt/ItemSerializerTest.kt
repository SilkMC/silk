package net.silkmc.silk.nbt

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import net.minecraft.SharedConstants
import net.minecraft.server.Bootstrap
import net.minecraft.world.item.ItemStack
import net.silkmc.silk.core.serialization.serializers.ResourceLocationSerializer
import net.silkmc.silk.nbt.dsl.nbtCompound
import net.silkmc.silk.nbt.serialization.Nbt
import net.silkmc.silk.nbt.serialization.serializer.ItemStackSerializer


@OptIn(ExperimentalSerializationApi::class)
private val nbt = Nbt {
    serializersModule = SerializersModule {
        contextual(ItemStackSerializer)
        contextual(ResourceLocationSerializer)
    }
}
private val itemTag = nbtCompound {
    put("id", "minecraft:grass")
    put("Count", 5.toByte())
    put("tag", nbtCompound { put("foo", "bar") })
}
private val item = ItemStack.of(itemTag)

@OptIn(ExperimentalSerializationApi::class)
class ItemSerializerTest : StringSpec({
    SharedConstants.tryDetectVersion()
    Bootstrap.bootStrap()
    "item stack should serialize correctly" {
        nbt.encodeToNbtElement(ItemStackSerializer, item) shouldBe itemTag
    }

    "item stack should deserialize correctly" {
        nbt.decodeFromNbtElement(ItemStackSerializer, itemTag) should { ItemStack.matches(it, item) }
    }
})