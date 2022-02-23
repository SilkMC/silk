package net.axay.fabrik.commands.internal

import com.mojang.brigadier.arguments.*
import net.minecraft.ChatFormatting
import net.minecraft.commands.arguments.*
import net.minecraft.commands.arguments.blocks.BlockInput
import net.minecraft.commands.arguments.blocks.BlockStateArgument
import net.minecraft.commands.arguments.coordinates.BlockPosArgument
import net.minecraft.commands.arguments.coordinates.Coordinates
import net.minecraft.commands.arguments.item.ItemArgument
import net.minecraft.commands.arguments.item.ItemInput
import net.minecraft.core.particles.ParticleOptions
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.item.enchantment.Enchantment
import java.util.*

object ArgumentTypeUtils {
    @Suppress("UNCHECKED_CAST")
    inline fun <reified T> fromReifiedType() = when (T::class) {
        Boolean::class -> BoolArgumentType.bool()
        Int::class -> IntegerArgumentType.integer()
        Long::class -> LongArgumentType.longArg()
        Float::class -> FloatArgumentType.floatArg()
        Double::class -> DoubleArgumentType.doubleArg()
        String::class -> StringArgumentType.string()

        Coordinates::class -> BlockPosArgument.blockPos()
        BlockInput::class -> BlockStateArgument.block()
        ChatFormatting::class -> ColorArgument.color()
        Enchantment::class -> ItemEnchantmentArgument.enchantment()
        ResourceLocation::class -> ResourceLocationArgument.id()
        ItemInput::class -> ItemArgument.item()
        CompoundTag::class -> CompoundTagArgument.compoundTag()
        Tag::class -> NbtTagArgument.nbtTag()
        ParticleOptions::class -> ParticleArgument.particle()
        MobEffect::class -> MobEffectArgument.effect()
        Component::class -> ComponentArgument.textComponent()
        UUID::class -> UuidArgument.uuid()

        else -> throw IllegalArgumentException("The specified type '${T::class.qualifiedName}' does not have corresponding default argument type")
    } as ArgumentType<T>
}
