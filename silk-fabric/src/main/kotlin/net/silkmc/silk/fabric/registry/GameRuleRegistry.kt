@file:Suppress("unused")

package net.silkmc.silk.fabric.registry

import net.fabricmc.fabric.api.gamerule.v1.CustomGameRuleCategory
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry
import net.fabricmc.fabric.api.gamerule.v1.rule.DoubleRule
import net.fabricmc.fabric.api.gamerule.v1.rule.EnumRule
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.world.level.GameRules
import kotlin.enums.enumEntries

fun <T : GameRules.Value<T>> gameruleOf(name: String, category: GameRules.Category, type: GameRules.Type<T>): GameRules.Key<T> {
    return GameRuleRegistry.register(name, category, type)
}

fun <T : GameRules.Value<T>> gameruleOf(name: String, category: CustomGameRuleCategory, type: GameRules.Type<T>): GameRules.Key<T> {
    return GameRuleRegistry.register(name, category, type)
}

fun gameruleCategoryOf(id: ResourceLocation, name: Component): CustomGameRuleCategory {
    return CustomGameRuleCategory(id, name)
}

fun gameruleCategoryOf(id: String, name: Component): CustomGameRuleCategory {
    return CustomGameRuleCategory(ResourceLocation.parse(id), name)
}

fun booleanGamerule(
    default: Boolean,
    callback: (MinecraftServer, GameRules.BooleanValue) -> Unit = { _, _ -> },
): GameRules.Type<GameRules.BooleanValue> {
    return GameRuleFactory.createBooleanRule(default, callback)
}

fun intGamerule(
    default: Int,
    min: Int = Int.MIN_VALUE,
    max: Int = Int.MAX_VALUE,
    callback: (MinecraftServer, GameRules.IntegerValue) -> Unit = { _, _ -> },
): GameRules.Type<GameRules.IntegerValue> {
    return GameRuleFactory.createIntRule(default, min, max, callback)
}

fun doubleGamerule(
    default: Double,
    min: Double = Double.MIN_VALUE,
    max: Double = Double.MAX_VALUE,
    callback: (MinecraftServer, DoubleRule) -> Unit = { _, _ -> },
): GameRules.Type<DoubleRule> {
    return GameRuleFactory.createDoubleRule(default, min, max, callback)
}

inline fun <reified E : Enum<E>> createEnumRule(
    default: E,
    values: List<E> = enumEntries<E>(),
    noinline callback: (MinecraftServer, EnumRule<E>) -> Unit = { _, _ -> },
): GameRules.Type<EnumRule<E>> {
    return GameRuleFactory.createEnumRule(default, values.toTypedArray(), callback)
}
