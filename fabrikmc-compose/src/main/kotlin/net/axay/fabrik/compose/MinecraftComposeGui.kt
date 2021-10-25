package net.axay.fabrik.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.ComposeScene
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.axay.fabrik.core.logging.logError
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.minecraft.block.MapColor
import net.minecraft.entity.decoration.ItemFrameEntity
import net.minecraft.item.Items
import net.minecraft.item.map.MapState
import net.minecraft.network.packet.c2s.play.HandSwingC2SPacket
import net.minecraft.network.packet.s2c.play.EntityTrackerUpdateS2CPacket
import net.minecraft.network.packet.s2c.play.MapUpdateS2CPacket
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Hand
import net.minecraft.util.math.*
import org.jetbrains.kotlinx.multik.api.linalg.dot
import org.jetbrains.kotlinx.multik.api.mk
import org.jetbrains.kotlinx.multik.api.ndarray
import org.jetbrains.kotlinx.multik.ndarray.data.D1Array
import org.jetbrains.kotlinx.multik.ndarray.data.get
import org.jetbrains.kotlinx.multik.ndarray.operations.minus
import org.jetbrains.kotlinx.multik.ndarray.operations.times
import org.jetbrains.skia.Bitmap
import org.jetbrains.skia.Canvas
import org.jetbrains.skiko.FrameDispatcher
import java.util.concurrent.Executors
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * Creates a new server-side gui based on composables.
 *
 * @param blockWidth the width of the gui **in blocks**
 * @param blockHeight the height of the gui **in blocks**
 * @param content define your gui using composable functions in here
 */
fun ServerPlayerEntity.displayComposable(
    blockWidth: Int, blockHeight: Int,
    position: BlockPos = blockPos.offset(horizontalFacing, 2),
    content: @Composable () -> Unit,
) = MinecraftComposeGui(
    blockWidth, blockHeight,
    content,
    this,
    position
)

class MinecraftComposeGui(
    val blockWidth: Int, val blockHeight: Int,
    val content: @Composable () -> Unit,
    val player: ServerPlayerEntity,
    val position: BlockPos,
) : CoroutineScope {
    companion object {
        private val playerGuis = HashMap<ServerPlayerEntity, MinecraftComposeGui>()

        init {
            ServerLifecycleEvents.SERVER_STOPPING.register {
                playerGuis.values.forEach { it.close() }
            }
        }

        // called by the mixin to ServerPlayNetworkHandler
        fun onLeftClick(player: ServerPlayerEntity, packet: HandSwingC2SPacket) {
            if (packet.hand == Hand.MAIN_HAND) {
                playerGuis[player]?.onLeftClick()
            }
        }

        private val mapColors = MapColor.COLORS
            .filterNotNull()
            .filterNot { it.color == 0 }
            .map {
                val javaColor = java.awt.Color(it.color)
                Color(javaColor.red, javaColor.green, javaColor.blue, javaColor.alpha) to it.id
            }
            .toTypedArray()

        private fun Vec3d.toMkArray() = mk.ndarray(doubleArrayOf(x, y, z))
        private fun Vec3i.toMkArray() = mk.ndarray(doubleArrayOf(x.toDouble(), y.toDouble(), z.toDouble()))
        private fun Vec3f.toMkArray() = mk.ndarray(doubleArrayOf(x.toDouble(), y.toDouble(), z.toDouble()))

        // taken from http://rosettacode.org/wiki/Find_the_intersection_of_a_line_with_a_plane#Kotlin
        private fun rayPlaneIntersection(
            rayPoint: D1Array<Double>,
            rayVector: D1Array<Double>,
            planePoint: D1Array<Double>,
            planeNormal: D1Array<Double>,
        ): D1Array<Double> {
            val diff = rayPoint - planePoint
            val prod1 = diff dot planeNormal
            val prod2 = rayVector dot planeNormal
            val prod3 = prod1 / prod2
            return rayPoint - rayVector * prod3
        }

        private infix fun Color.colorDistance(c2: Color) =
            sqrt((c2.red - red).pow(2) + (c2.green - green).pow(2) + (c2.blue - blue).pow(2))

        private fun BlockPos.withoutAxis(axis: Direction.Axis) = when (axis) {
            Direction.Axis.X -> z.toDouble() to y.toDouble()
            Direction.Axis.Z -> x.toDouble() to y.toDouble()
            // just for the compiler
            Direction.Axis.Y -> x.toDouble() to z.toDouble()
        }

        private fun D1Array<Double>.withoutAxis(axis: Direction.Axis) = when (axis) {
            Direction.Axis.X -> this[2] to this[1]
            Direction.Axis.Z -> this[0] to this[1]
            // just for the compiler
            Direction.Axis.Y -> this[0] to this[2]
        }
    }

    private class GuiChunk(val mapId: Int, val colors: ByteArray = ByteArray(128 * 128)) {
        constructor(world: ServerWorld) : this(world.nextMapId) // TODO use a custom ID generator

        operator fun component1() = mapId
        operator fun component2() = colors
    }

    override val coroutineContext = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

    // values for geometry

    private val guiDirection = player.horizontalFacing.opposite
    private val placementDirection = guiDirection.rotateCounterclockwise(Direction.Axis.Y)

    // the perceived position of the display is one behind the actual item frame position
    private val displayPosition = when (guiDirection) {
        Direction.EAST, Direction.SOUTH -> position
        Direction.WEST, Direction.NORTH -> position.offset(guiDirection.opposite)
        else -> position
    }

    private val planePoint = displayPosition.toMkArray()
    private val planeNormal = guiDirection.unitVector.toMkArray()

    private val topCorner = displayPosition.down(blockHeight - 1).offset(
        placementDirection,
        blockWidth - (if (guiDirection == Direction.WEST || guiDirection == Direction.SOUTH) 0 else 1)
    ).withoutAxis(guiDirection.axis)
    private val bottomCorner = displayPosition.offset(placementDirection.opposite).up().offset(
        placementDirection,
        if (guiDirection == Direction.WEST || guiDirection == Direction.SOUTH) 1 else 0
    ).withoutAxis(guiDirection.axis)

    // values for rendering

    private val frameDispatcher = FrameDispatcher(coroutineContext) { updateMinecraftMaps() }

    private val bitmap = Bitmap().also {
        if (!it.allocN32Pixels(blockWidth * 128, blockHeight * 128, true))
            logError("Could not allocate the required resources for rendering the compose gui!")
    }

    private val scene = ComposeScene(coroutineContext) { frameDispatcher.scheduleFrame() }
    private val canvas = Canvas(bitmap)

    private val guiChunks = Array(blockWidth * blockHeight) { GuiChunk(player.serverWorld) }

    private fun getGuiChunk(x: Int, y: Int) = guiChunks[x + y * blockWidth]

    private var placedItemFrames = false

    init {
        scene.setContent {
            Box(Modifier.size((blockWidth * 128).dp, (blockHeight * 128).dp)) {
                content()
            }
        }

        playerGuis[player]?.close()
        playerGuis[player] = this
    }

    private fun updateMinecraftMaps() {
        scene.render(canvas, System.nanoTime())

        val networkHandler = player.networkHandler

        for (xFrame in 0 until blockWidth) {
            for (yFrame in 0 until blockHeight) {
                val (mapId, colors) = getGuiChunk(xFrame, yFrame)

                for (x in 0 until 128) {
                    for (y in 0 until 128) {
                        val bitmapColor = Color(bitmap.getColor(xFrame * 128 + x, yFrame * 128 + y))

                        val mapColor = if (bitmapColor.alpha == 0f) MapColor.WHITE.id else mapColors.minByOrNull { it.first colorDistance bitmapColor }!!.second
                        val byteColor = (mapColor * 4).toByte()

                        colors[x + y * 128] = byteColor
                    }
                }

                // send the map data
                networkHandler.sendPacket(MapUpdateS2CPacket(mapId, 0, false, emptyList(), MapState.UpdateData(0, 0, 128, 128, colors)))

                if (!placedItemFrames) {
                    val framePos = position.down(yFrame).offset(placementDirection, xFrame)

                    // spawn the fake item frame
                    val itemFrame = ItemFrameEntity(player.world, framePos, guiDirection)
                    itemFrame.isInvisible = true
                    networkHandler.sendPacket(itemFrame.createSpawnPacket())

                    // put the map in the item frame
                    val composeStack = Items.FILLED_MAP.defaultStack.apply {
                        orCreateNbt.putInt("map", mapId)
                    }
                    itemFrame.setHeldItemStack(composeStack, false)
                    networkHandler.sendPacket(EntityTrackerUpdateS2CPacket(itemFrame.id, itemFrame.dataTracker, true))
                }
            }
        }

        if (!placedItemFrames) placedItemFrames = true
    }

    private fun onLeftClick() = launch {
        val intersection = rayPlaneIntersection(
            player.eyePos.toMkArray(),
            player.rotationVector.toMkArray(),
            planePoint,
            planeNormal
        )

        val (worldX, worldY) = intersection.withoutAxis(guiDirection.axis)

        val (topX, topY) = topCorner
        val (bottomX, bottomY) = bottomCorner

        val planeX = when (worldX) {
            in bottomX..topX -> worldX - bottomX
            in topX..bottomX -> bottomX - worldX
            else -> return@launch
        }

        val planeY = when (worldY) {
            in bottomY..topY -> worldY - bottomY
            in topY..bottomY -> bottomY - worldY
            else -> return@launch
        }

        val offset = Offset((planeX * 128).toFloat(), (planeY * 128).toFloat())

        scene.sendPointerEvent(PointerEventType.Press, offset)
        delay(20)
        scene.sendPointerEvent(PointerEventType.Release, offset)
    }

    fun close() {
        coroutineContext.close()
        scene.close()
    }
}
