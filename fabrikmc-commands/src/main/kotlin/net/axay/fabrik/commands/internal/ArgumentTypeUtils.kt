package net.axay.fabrik.commands.internal

import com.mojang.brigadier.arguments.*
import net.minecraft.command.argument.*
import net.minecraft.enchantment.Enchantment
import net.minecraft.entity.effect.StatusEffect
import net.minecraft.nbt.NbtCompound
import net.minecraft.nbt.NbtElement
import net.minecraft.particle.ParticleEffect
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
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

        PosArgument::class -> BlockPosArgumentType.blockPos()
        BlockStateArgument::class -> BlockStateArgumentType.blockState()
        Formatting::class -> ColorArgumentType.color()
        Enchantment::class -> EnchantmentArgumentType.enchantment()
        Identifier::class -> IdentifierArgumentType.identifier()
        ItemStackArgument::class -> ItemStackArgumentType.itemStack()
        NbtCompound::class -> NbtCompoundArgumentType.nbtCompound()
        NbtElement::class -> NbtElementArgumentType.nbtElement()
        ParticleEffect::class -> ParticleEffectArgumentType.particleEffect()
        StatusEffect::class -> StatusEffectArgumentType.statusEffect()
        Text::class -> TextArgumentType.text()
        UUID::class -> UuidArgumentType.uuid()

        else -> throw IllegalArgumentException("The specified type '${T::class.qualifiedName}' does not have corresponding default argument type")
    } as ArgumentType<T>
}
