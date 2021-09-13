package net.axay.fabrik.core.math

import kotlinx.serialization.Serializable
import net.axay.fabrik.core.Fabrik
import net.axay.fabrik.core.serialization.serializers.IdentifierSerializer
import net.minecraft.entity.Entity
import net.minecraft.util.Identifier
import net.minecraft.util.math.*
import net.minecraft.util.registry.Registry
import net.minecraft.util.registry.RegistryKey
import net.minecraft.world.World
import kotlin.math.roundToInt

/**
 * A class which can be initialized with all kinds of positions and has the
 * ability to convert them to nearly every other kind of position.
 *
 * Additionally, this class is serializable.
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
@Serializable
data class FabrikPosition(
    val x: Double = 0.0, val y: Double = 0.0, val z: Double = 0.0,
    @Serializable(with = IdentifierSerializer::class) val worldIdentifier: Identifier? = null,
    val pitch: Float = 0f, val yaw: Float = 0f,
) {
    constructor(blockPos: BlockPos, worldIdentifier: Identifier? = null, pitch: Float = 0f, yaw: Float = 0f)
            : this(blockPos.x.toDouble(), blockPos.y.toDouble(), blockPos.z.toDouble(), worldIdentifier, pitch, yaw)

    constructor(vec3i: Vec3i, worldIdentifier: Identifier? = null, pitch: Float = 0f, yaw: Float = 0f)
            : this(vec3i.x.toDouble(), vec3i.y.toDouble(), vec3i.z.toDouble(), worldIdentifier, pitch, yaw)

    constructor(vec3f: Vec3f, worldIdentifier: Identifier? = null, pitch: Float = 0f, yaw: Float = 0f)
            : this(vec3f.x.toDouble(), vec3f.y.toDouble(), vec3f.z.toDouble(), worldIdentifier, pitch, yaw)

    constructor(vec3d: Vec3d, worldIdentifier: Identifier? = null, pitch: Float = 0f, yaw: Float = 0f)
            : this(vec3d.x, vec3d.y, vec3d.z, worldIdentifier, pitch, yaw)

    constructor(entity: Entity)
            : this(entity.pos, entity.world.registryKey.value, entity.pitch, entity.yaw)

    constructor(chunkPos: ChunkPos, worldIdentifier: Identifier? = null, pitch: Float = 0f, yaw: Float = 0f) : this(
        ChunkSectionPos.getBlockCoord(chunkPos.x).toDouble(), 0.0, ChunkSectionPos.getBlockCoord(chunkPos.z).toDouble(),
        worldIdentifier,
        pitch, yaw
    )

    constructor(chunkSectionPos: ChunkSectionPos, worldIdentifier: Identifier? = null, pitch: Float = 0f, yaw: Float = 0f) : this(
        ChunkSectionPos.getBlockCoord(chunkSectionPos.x).toDouble(), ChunkSectionPos.getBlockCoord(chunkSectionPos.y).toDouble(), ChunkSectionPos.getBlockCoord(chunkSectionPos.z).toDouble(),
        worldIdentifier,
        pitch, yaw
    )

    val worldKey: RegistryKey<World> get() = RegistryKey.of(Registry.WORLD_KEY, worldIdentifier)
    val world get() = if (worldIdentifier != null) Fabrik.currentServer?.getWorld(worldKey) else null
    val blockPos get() = BlockPos(x.toInt(), y.toInt(), z.toInt())
    val roundedBlockPos get() = BlockPos(x.roundToInt(), y.roundToInt(), z.roundToInt())
    val posInChunk get() = PositionInChunk(blockPos)
    val chunkPos get() = ChunkPos(ChunkSectionPos.getSectionCoord(x), ChunkSectionPos.getSectionCoord(z))
    val chunkSectionPos: ChunkSectionPos get() = ChunkSectionPos.from(blockPos)
    val vec3i get() = Vec3i(x, y, z)
    val vec3f get() = Vec3f(x.toFloat(), y.toFloat(), z.toFloat())
    val vec3d get() = Vec3d(x, y, z)
}
