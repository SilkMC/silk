package net.silkmc.silk.commands.internal

import com.mojang.brigadier.arguments.*
import net.minecraft.ChatFormatting
import net.minecraft.commands.CommandBuildContext
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
import java.util.*

object ArgumentTypeUtils {

    /**
     * Converts the given reified type [T] to an [ArgumentType].
     * Note that this function fails if there is no corresponding
     * [ArgumentType] mapped to [T].
     *
     * @param context the context which provides access to registries
     */
    inline fun <reified T> fromReifiedType(context: CommandBuildContext): ArgumentType<T> {
        val type = when (T::class) {
            Boolean::class -> BoolArgumentType.bool()
            Int::class -> IntegerArgumentType.integer()
            Long::class -> LongArgumentType.longArg()
            Float::class -> FloatArgumentType.floatArg()
            Double::class -> DoubleArgumentType.doubleArg()
            String::class -> StringArgumentType.string()

            UUID::class -> UuidArgument.uuid()

            Coordinates::class -> BlockPosArgument.blockPos()
            BlockInput::class -> BlockStateArgument.block(context)
            ChatFormatting::class -> ColorArgument.color()
            ResourceLocation::class -> ResourceLocationArgument.id()
            ItemInput::class -> ItemArgument.item(context)
            CompoundTag::class -> CompoundTagArgument.compoundTag()
            Tag::class -> NbtTagArgument.nbtTag()
            ParticleOptions::class -> ParticleArgument.particle(context)
            Component::class -> ComponentArgument.textComponent(context)
            GameProfileArgument.Result::class -> GameProfileArgument.gameProfile()

            // see ResourceArgument
            // Enchantment::class -> ItemEnchantmentArgument.enchantment()
            // MobEffect::class -> MobEffectArgument.effect()

            else -> throw IllegalArgumentException("The specified type '${T::class.qualifiedName}' does not have corresponding default argument type")
        }

        @Suppress("UNCHECKED_CAST")
        return type as ArgumentType<T>
    }
}
