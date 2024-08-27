package net.silkmc.silk.core.registry

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory

fun <T : Entity> entityTypeOf(
    category: MobCategory,
    factory: EntityType.EntityFactory<T>,
    builder: EntityType.Builder<T>.() -> Unit,
): EntityType<T> {
    val entityTypeBuilder = EntityType.Builder.of(factory, category)

    return entityTypeBuilder.apply(builder).build()
}

fun <T : Entity> EntityType<T>.register(id: ResourceLocation): EntityType<T> {
    return BuiltInRegistries.ENTITY_TYPE.register(id, this)
}

fun <T : Entity> EntityType<T>.register(id: String): EntityType<T> {
    return BuiltInRegistries.ENTITY_TYPE.register(id, this)
}
