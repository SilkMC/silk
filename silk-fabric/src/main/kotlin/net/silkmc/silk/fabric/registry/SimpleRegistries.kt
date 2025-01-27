@file:Suppress("unused")

package net.silkmc.silk.fabric.registry

import net.minecraft.advancements.critereon.ItemSubPredicate
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.numbers.NumberFormat
import net.minecraft.network.chat.numbers.NumberFormatType
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.valueproviders.FloatProvider
import net.minecraft.util.valueproviders.FloatProviderType
import net.minecraft.util.valueproviders.IntProvider
import net.minecraft.util.valueproviders.IntProviderType
import net.minecraft.world.entity.ai.attributes.Attribute
import net.minecraft.world.entity.ai.memory.MemoryModuleType
import net.minecraft.world.entity.ai.sensing.Sensor
import net.minecraft.world.entity.ai.sensing.SensorType
import net.minecraft.world.entity.ai.village.poi.PoiType
import net.minecraft.world.entity.animal.CatVariant
import net.minecraft.world.entity.animal.FrogVariant
import net.minecraft.world.entity.npc.VillagerProfession
import net.minecraft.world.entity.npc.VillagerType
import net.minecraft.world.entity.schedule.Activity
import net.minecraft.world.entity.schedule.Schedule
import net.minecraft.world.level.block.entity.DecoratedPotPattern
import net.minecraft.world.level.gameevent.GameEvent
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicateType
import net.minecraft.world.level.levelgen.carver.CarverConfiguration
import net.minecraft.world.level.levelgen.carver.WorldCarver
import net.minecraft.world.level.levelgen.feature.Feature
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration
import net.minecraft.world.level.levelgen.feature.featuresize.FeatureSize
import net.minecraft.world.level.levelgen.feature.featuresize.FeatureSizeType
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType
import net.minecraft.world.level.levelgen.feature.rootplacers.RootPlacer
import net.minecraft.world.level.levelgen.feature.rootplacers.RootPlacerType
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProviderType
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider
import net.minecraft.world.level.levelgen.heightproviders.HeightProviderType
import net.minecraft.world.level.levelgen.placement.PlacementModifier
import net.minecraft.world.level.levelgen.placement.PlacementModifierType
import net.minecraft.world.level.levelgen.structure.Structure
import net.minecraft.world.level.levelgen.structure.StructureType
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElementType
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType
import net.minecraft.world.level.saveddata.maps.MapDecorationType
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryType
import net.minecraft.world.level.storage.loot.functions.LootItemFunction
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType
import net.minecraft.world.level.storage.loot.providers.nbt.LootNbtProviderType
import net.minecraft.world.level.storage.loot.providers.number.LootNumberProviderType
import net.minecraft.world.level.storage.loot.providers.score.LootScoreProviderType

fun GameEvent.register(id: ResourceLocation): GameEvent = BuiltInRegistries.GAME_EVENT.register(id, this)
fun GameEvent.register(id: String): GameEvent = BuiltInRegistries.GAME_EVENT.register(id, this)


fun Attribute.register(id: ResourceLocation): Attribute = BuiltInRegistries.ATTRIBUTE.register(id, this)
fun Attribute.register(id: String): Attribute = BuiltInRegistries.ATTRIBUTE.register(id, this)


fun VillagerType.register(id: ResourceLocation): VillagerType = BuiltInRegistries.VILLAGER_TYPE.register(id, this)
fun VillagerType.register(id: String): VillagerType = BuiltInRegistries.VILLAGER_TYPE.register(id, this)


fun VillagerProfession.register(id: ResourceLocation): VillagerProfession = BuiltInRegistries.VILLAGER_PROFESSION.register(id, this)
fun VillagerProfession.register(id: String): VillagerProfession = BuiltInRegistries.VILLAGER_PROFESSION.register(id, this)


fun PoiType.register(id: ResourceLocation): PoiType = BuiltInRegistries.POINT_OF_INTEREST_TYPE.register(id, this)
fun PoiType.register(id: String): PoiType = BuiltInRegistries.POINT_OF_INTEREST_TYPE.register(id, this)


fun <T> MemoryModuleType<T>.register(id: ResourceLocation): MemoryModuleType<T> = BuiltInRegistries.MEMORY_MODULE_TYPE.register(id, this)
fun <T> MemoryModuleType<T>.register(id: String): MemoryModuleType<T> = BuiltInRegistries.MEMORY_MODULE_TYPE.register(id, this)


fun <T : Sensor<*>> SensorType<T>.register(id: ResourceLocation): SensorType<T> = BuiltInRegistries.SENSOR_TYPE.register(id, this)
fun <T : Sensor<*>> SensorType<T>.register(id: String): SensorType<T> = BuiltInRegistries.SENSOR_TYPE.register(id, this)


fun Schedule.register(id: ResourceLocation): Schedule = BuiltInRegistries.SCHEDULE.register(id, this)
fun Schedule.register(id: String): Schedule = BuiltInRegistries.SCHEDULE.register(id, this)


fun Activity.register(id: ResourceLocation): Activity = BuiltInRegistries.ACTIVITY.register(id, this)
fun Activity.register(id: String): Activity = BuiltInRegistries.ACTIVITY.register(id, this)


fun LootPoolEntryType.register(id: ResourceLocation): LootPoolEntryType = BuiltInRegistries.LOOT_POOL_ENTRY_TYPE.register(id, this)
fun LootPoolEntryType.register(id: String): LootPoolEntryType = BuiltInRegistries.LOOT_POOL_ENTRY_TYPE.register(id, this)


fun <T : LootItemFunction> LootItemFunctionType<T>.register(id: ResourceLocation): LootItemFunctionType<T> {
    return BuiltInRegistries.LOOT_FUNCTION_TYPE.register(id, this)
}

fun <T : LootItemFunction> LootItemFunctionType<T>.register(id: String): LootItemFunctionType<T> {
    return BuiltInRegistries.LOOT_FUNCTION_TYPE.register(id, this)
}


fun LootItemConditionType.register(id: ResourceLocation): LootItemConditionType = BuiltInRegistries.LOOT_CONDITION_TYPE.register(id, this)
fun LootItemConditionType.register(id: String): LootItemConditionType = BuiltInRegistries.LOOT_CONDITION_TYPE.register(id, this)


fun LootNumberProviderType.register(id: ResourceLocation): LootNumberProviderType {
    return BuiltInRegistries.LOOT_NUMBER_PROVIDER_TYPE.register(id, this)
}

fun LootNumberProviderType.register(id: String): LootNumberProviderType {
    return BuiltInRegistries.LOOT_NUMBER_PROVIDER_TYPE.register(id, this)
}


fun LootNbtProviderType.register(id: ResourceLocation): LootNbtProviderType = BuiltInRegistries.LOOT_NBT_PROVIDER_TYPE.register(id, this)
fun LootNbtProviderType.register(id: String): LootNbtProviderType = BuiltInRegistries.LOOT_NBT_PROVIDER_TYPE.register(id, this)


fun LootScoreProviderType.register(id: ResourceLocation): LootScoreProviderType {
    return BuiltInRegistries.LOOT_SCORE_PROVIDER_TYPE.register(id, this)
}

fun LootScoreProviderType.register(id: String): LootScoreProviderType {
    return BuiltInRegistries.LOOT_SCORE_PROVIDER_TYPE.register(id, this)
}


fun <T : FloatProvider> FloatProviderType<T>.register(id: ResourceLocation): FloatProviderType<T> {
    return BuiltInRegistries.FLOAT_PROVIDER_TYPE.register(id, this)
}

fun <T : FloatProvider> FloatProviderType<T>.register(id: String): FloatProviderType<T> {
    return BuiltInRegistries.FLOAT_PROVIDER_TYPE.register(id, this)
}


fun <T : IntProvider> IntProviderType<T>.register(id: ResourceLocation): IntProviderType<T> {
    return BuiltInRegistries.INT_PROVIDER_TYPE.register(id, this)
}

fun <T : IntProvider> IntProviderType<T>.register(id: String): IntProviderType<T> {
    return BuiltInRegistries.INT_PROVIDER_TYPE.register(id, this)
}


fun <T : HeightProvider> HeightProviderType<T>.register(id: ResourceLocation): HeightProviderType<T> {
    return BuiltInRegistries.HEIGHT_PROVIDER_TYPE.register(id, this)
}

fun <T : HeightProvider> HeightProviderType<T>.register(id: String): HeightProviderType<T> {
    return BuiltInRegistries.HEIGHT_PROVIDER_TYPE.register(id, this)
}


fun <T : BlockPredicate> BlockPredicateType<T>.register(id: ResourceLocation): BlockPredicateType<T> {
    return BuiltInRegistries.BLOCK_PREDICATE_TYPE.register(id, this)
}

fun <T : BlockPredicate> BlockPredicateType<T>.register(id: String): BlockPredicateType<T> {
    return BuiltInRegistries.BLOCK_PREDICATE_TYPE.register(id, this)
}


fun <T : CarverConfiguration> WorldCarver<T>.register(id: ResourceLocation): WorldCarver<T> {
    return BuiltInRegistries.CARVER.register(id, this)
}

fun <T : CarverConfiguration> WorldCarver<T>.register(id: String): WorldCarver<T> {
    return BuiltInRegistries.CARVER.register(id, this)
}


fun <T : FeatureConfiguration> Feature<T>.register(id: ResourceLocation): Feature<T> {
    return BuiltInRegistries.FEATURE.register(id, this)
}

fun <T : FeatureConfiguration> Feature<T>.register(id: String): Feature<T> {
    return BuiltInRegistries.FEATURE.register(id, this)
}


fun <T : StructurePlacement> StructurePlacementType<T>.register(id: ResourceLocation): StructurePlacementType<T> {
    return BuiltInRegistries.STRUCTURE_PLACEMENT.register(id, this)
}

fun <T : StructurePlacement> StructurePlacementType<T>.register(id: String): StructurePlacementType<T> {
    return BuiltInRegistries.STRUCTURE_PLACEMENT.register(id, this)
}


fun StructurePieceType.register(id: ResourceLocation): StructurePieceType = BuiltInRegistries.STRUCTURE_PIECE.register(id, this)
fun StructurePieceType.register(id: String): StructurePieceType = BuiltInRegistries.STRUCTURE_PIECE.register(id, this)


fun <T : Structure> StructureType<T>.register(id: ResourceLocation): StructureType<T> {
    return BuiltInRegistries.STRUCTURE_TYPE.register(id, this)
}

fun <T : Structure> StructureType<T>.register(id: String): StructureType<T> {
    return BuiltInRegistries.STRUCTURE_TYPE.register(id, this)
}


fun <T : PlacementModifier> PlacementModifierType<T>.register(id: ResourceLocation): PlacementModifierType<T> {
    return BuiltInRegistries.PLACEMENT_MODIFIER_TYPE.register(id, this)
}

fun <T : PlacementModifier> PlacementModifierType<T>.register(id: String): PlacementModifierType<T> {
    return BuiltInRegistries.PLACEMENT_MODIFIER_TYPE.register(id, this)
}


fun <T : BlockStateProvider> BlockStateProviderType<T>.register(id: ResourceLocation): BlockStateProviderType<T> {
    return BuiltInRegistries.BLOCKSTATE_PROVIDER_TYPE.register(id, this)
}

fun <T : BlockStateProvider> BlockStateProviderType<T>.register(id: String): BlockStateProviderType<T> {
    return BuiltInRegistries.BLOCKSTATE_PROVIDER_TYPE.register(id, this)
}


fun <T : FoliagePlacer> FoliagePlacerType<T>.register(id: ResourceLocation): FoliagePlacerType<T> {
    return BuiltInRegistries.FOLIAGE_PLACER_TYPE.register(id, this)
}

fun <T : FoliagePlacer> FoliagePlacerType<T>.register(id: String): FoliagePlacerType<T> {
    return BuiltInRegistries.FOLIAGE_PLACER_TYPE.register(id, this)
}


fun <T : TrunkPlacer> TrunkPlacerType<T>.register(id: ResourceLocation): TrunkPlacerType<T> {
    return BuiltInRegistries.TRUNK_PLACER_TYPE.register(id, this)
}

fun <T : TrunkPlacer> TrunkPlacerType<T>.register(id: String): TrunkPlacerType<T> {
    return BuiltInRegistries.TRUNK_PLACER_TYPE.register(id, this)
}


fun <T : RootPlacer> RootPlacerType<T>.register(id: ResourceLocation): RootPlacerType<T> {
    return BuiltInRegistries.ROOT_PLACER_TYPE.register(id, this)
}

fun <T : RootPlacer> RootPlacerType<T>.register(id: String): RootPlacerType<T> {
    return BuiltInRegistries.ROOT_PLACER_TYPE.register(id, this)
}


fun <T : TreeDecorator> TreeDecoratorType<T>.register(id: ResourceLocation): TreeDecoratorType<T> {
    return BuiltInRegistries.TREE_DECORATOR_TYPE.register(id, this)
}

fun <T : TreeDecorator> TreeDecoratorType<T>.register(id: String): TreeDecoratorType<T> {
    return BuiltInRegistries.TREE_DECORATOR_TYPE.register(id, this)
}


fun <T : FeatureSize> FeatureSizeType<T>.register(id: ResourceLocation): FeatureSizeType<T> {
    return BuiltInRegistries.FEATURE_SIZE_TYPE.register(id, this)
}

fun <T : FeatureSize> FeatureSizeType<T>.register(id: String): FeatureSizeType<T> {
    return BuiltInRegistries.FEATURE_SIZE_TYPE.register(id, this)
}


fun <T : StructureProcessor> StructureProcessorType<T>.register(id: ResourceLocation): StructureProcessorType<T> {
    return BuiltInRegistries.STRUCTURE_PROCESSOR.register(id, this)
}

fun <T : StructureProcessor> StructureProcessorType<T>.register(id: String): StructureProcessorType<T> {
    return BuiltInRegistries.STRUCTURE_PROCESSOR.register(id, this)
}


fun <T : StructurePoolElement> StructurePoolElementType<T>.register(id: ResourceLocation): StructurePoolElementType<T> {
    return BuiltInRegistries.STRUCTURE_POOL_ELEMENT.register(id, this)
}

fun <T : StructurePoolElement> StructurePoolElementType<T>.register(id: String): StructurePoolElementType<T> {
    return BuiltInRegistries.STRUCTURE_POOL_ELEMENT.register(id, this)
}


fun CatVariant.register(id: ResourceLocation): CatVariant = BuiltInRegistries.CAT_VARIANT.register(id, this)
fun CatVariant.register(id: String): CatVariant = BuiltInRegistries.CAT_VARIANT.register(id, this)


fun FrogVariant.register(id: ResourceLocation): FrogVariant = BuiltInRegistries.FROG_VARIANT.register(id, this)
fun FrogVariant.register(id: String): FrogVariant = BuiltInRegistries.FROG_VARIANT.register(id, this)

fun DecoratedPotPattern.register(id: ResourceLocation): DecoratedPotPattern = BuiltInRegistries.DECORATED_POT_PATTERN.register(id, this)
fun DecoratedPotPattern.register(id: String): DecoratedPotPattern = BuiltInRegistries.DECORATED_POT_PATTERN.register(id, this)


fun <T : NumberFormat> NumberFormatType<T>.register(id: ResourceLocation): NumberFormatType<T> {
    return BuiltInRegistries.NUMBER_FORMAT_TYPE.register(id, this)
}

fun <T : NumberFormat> NumberFormatType<T>.register(id: String): NumberFormatType<T> {
    return BuiltInRegistries.NUMBER_FORMAT_TYPE.register(id, this)
}


fun <T : ItemSubPredicate> ItemSubPredicate.Type<T>.register(id: ResourceLocation): ItemSubPredicate.Type<T> {
    return BuiltInRegistries.ITEM_SUB_PREDICATE_TYPE.register(id, this)
}

fun <T : ItemSubPredicate> ItemSubPredicate.Type<T>.register(id: String): ItemSubPredicate.Type<T> {
    return BuiltInRegistries.ITEM_SUB_PREDICATE_TYPE.register(id, this)
}


fun MapDecorationType.register(id: ResourceLocation): MapDecorationType = BuiltInRegistries.MAP_DECORATION_TYPE.register(id, this)
fun MapDecorationType.register(id: String): MapDecorationType = BuiltInRegistries.MAP_DECORATION_TYPE.register(id, this)
