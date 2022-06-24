package net.silkmc.silk.core.world.pos

import com.mojang.math.Vector3f
import kotlinx.serialization.Serializable
import net.minecraft.core.BlockPos
import net.minecraft.core.Registry
import net.minecraft.core.SectionPos
import net.minecraft.core.Vec3i
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.ChunkPos
import net.minecraft.world.level.Level
import net.minecraft.world.phys.Vec3
import net.silkmc.silk.core.Silk
import net.silkmc.silk.core.entity.pos
import net.silkmc.silk.core.serialization.serializers.ResourceLocationSerializer
import kotlin.math.roundToInt

@Deprecated(
    message = "FabrikMC has been renamed to Silk.",
    replaceWith = ReplaceWith("net.silkmc.silk.core.world.pos.SilkPosition")
)
typealias FabrikPosition = SilkPosition

/**
 * A class which can be initialized with all kinds of positions and has the
 * ability to convert them to nearly every other kind of position.
 *
 * Additionally, this class is serializable.
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
@Serializable
data class SilkPosition(
    val x: Double = 0.0, val y: Double = 0.0, val z: Double = 0.0,
    @Serializable(with = ResourceLocationSerializer::class) val worldIdentifier: ResourceLocation? = null,
    val pitch: Float = 0f, val yaw: Float = 0f,
) {
    constructor(blockPos: BlockPos, worldIdentifier: ResourceLocation? = null, pitch: Float = 0f, yaw: Float = 0f)
        : this(blockPos.x.toDouble(), blockPos.y.toDouble(), blockPos.z.toDouble(), worldIdentifier, pitch, yaw)

    constructor(vec3i: Vec3i, worldIdentifier: ResourceLocation? = null, pitch: Float = 0f, yaw: Float = 0f)
        : this(vec3i.x.toDouble(), vec3i.y.toDouble(), vec3i.z.toDouble(), worldIdentifier, pitch, yaw)

    constructor(vec3f: Vector3f, worldIdentifier: ResourceLocation? = null, pitch: Float = 0f, yaw: Float = 0f)
        : this(vec3f.x().toDouble(), vec3f.y().toDouble(), vec3f.z().toDouble(), worldIdentifier, pitch, yaw)

    constructor(vec3d: Vec3, worldIdentifier: ResourceLocation? = null, pitch: Float = 0f, yaw: Float = 0f)
        : this(vec3d.x, vec3d.y, vec3d.z, worldIdentifier, pitch, yaw)

    constructor(entity: Entity)
        : this(entity.pos, entity.level.dimension().location(), entity.xRot, entity.yRot)

    constructor(chunkPos: ChunkPos, worldIdentifier: ResourceLocation? = null, pitch: Float = 0f, yaw: Float = 0f) : this(
        chunkPos.minBlockX.toDouble(), 0.0, chunkPos.minBlockZ.toDouble(),
        worldIdentifier,
        pitch, yaw
    )

    constructor(chunkSectionPos: SectionPos, worldIdentifier: ResourceLocation? = null, pitch: Float = 0f, yaw: Float = 0f) : this(
        chunkSectionPos.minBlockX().toDouble(), chunkSectionPos.minBlockY().toDouble(), chunkSectionPos.minBlockZ().toDouble(),
        worldIdentifier,
        pitch, yaw
    )

    val blockPos: BlockPos
        get() = BlockPos(x.toInt(), y.toInt(), z.toInt())
    val roundedBlockPos: BlockPos
        get() = BlockPos(x.roundToInt(), y.roundToInt(), z.roundToInt())
    val posInChunk: PosInChunk
        get() = PosInChunk(blockPos)
    val chunkPos: ChunkPos
        get() = ChunkPos(SectionPos.posToSectionCoord(x), SectionPos.posToSectionCoord(z))
    val chunkSectionPos: SectionPos
        get() = SectionPos.of(blockPos)

    val vec3i: Vec3i
        get() = Vec3i(x, y, z)
    val vec3f: Vector3f
        get() = Vector3f(x.toFloat(), y.toFloat(), z.toFloat())
    val vec3d: Vec3
        get() = Vec3(x, y, z)

    val worldKey: ResourceKey<Level>?
        get() = if (worldIdentifier != null) ResourceKey.create(Registry.DIMENSION_REGISTRY, worldIdentifier) else null
    val world: Level?
        get() = worldKey?.let { Silk.currentServer?.getLevel(it) }
}
