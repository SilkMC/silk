@file:UseSerializers(IdentifierSerializer::class)

package net.axay.fabrik.core.math

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import net.axay.fabrik.core.Fabrik
import net.axay.fabrik.core.serialization.serializers.IdentifierSerializer
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Vec3d
import net.minecraft.util.registry.Registry
import net.minecraft.util.registry.RegistryKey
import net.minecraft.world.World
import kotlin.math.roundToInt

@Serializable
class FabrikPosition(
    val x: Double, val y: Double, val z: Double,
    val worldIdentifier: Identifier? = null,
    val pitch: Float = 0f, val yaw: Float = 0f,
) {
    constructor(
        blockPos: BlockPos,
        worldIdentifier: Identifier? = null,
        pitch: Float = 0f, yaw: Float = 0f,
    ) : this(
        blockPos.x.toDouble(), blockPos.y.toDouble(), blockPos.z.toDouble(),
        worldIdentifier,
        pitch, yaw,
    )

    constructor(
        vec3d: Vec3d,
        worldIdentifier: Identifier? = null,
        pitch: Float = 0f, yaw: Float = 0f,
    ) : this(
        vec3d.x, vec3d.y, vec3d.z,
        worldIdentifier,
        pitch, yaw,
    )

    constructor(player: ServerPlayerEntity) : this(player.pos, player.world.registryKey.value, player.pitch, player.yaw)

    val worldKey: RegistryKey<World> get() = RegistryKey.of(Registry.WORLD_KEY, worldIdentifier)
    val world get() = if (worldIdentifier != null) Fabrik.currentServer?.getWorld(worldKey) else null
    val blockPos get() = BlockPos(x.toInt(), y.toInt(), z.toInt())
    val roundedBlockPos get() = BlockPos(x.roundToInt(), y.roundToInt(), z.roundToInt())
    val posInChunk get() = PositionInChunk(blockPos)
}
