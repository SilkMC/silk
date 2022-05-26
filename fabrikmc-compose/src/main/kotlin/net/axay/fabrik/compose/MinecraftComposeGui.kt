package net.axay.fabrik.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.ComposeScene
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.unit.dp
import com.github.ajalt.colormath.model.SRGB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.launch
import net.axay.fabrik.compose.color.MaterialColorUtils
import net.axay.fabrik.compose.internal.MapIdGenerator
import net.axay.fabrik.core.logging.logError
import net.axay.fabrik.core.logging.logInfo
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.Vec3i
import net.minecraft.network.protocol.game.*
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.decoration.GlowItemFrame
import net.minecraft.world.item.Items
import net.minecraft.world.level.saveddata.maps.MapItemSavedData
import net.minecraft.world.phys.Vec3
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
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import kotlin.math.max
import kotlin.math.min

/**
 * Creates a new server-side [MinecraftComposeGui]. This allows you to use any
 * composable functions inside the [content] lambda of this function.
 *
 * @param blockWidth the width of the gui **in blocks**
 * @param blockHeight the height of the gui **in blocks**
 * @param content define your gui using composable functions in here
 *
 * @see MinecraftComposeGui
 */
fun ServerPlayer.displayComposable(
    blockWidth: Int, blockHeight: Int,
    position: BlockPos = blockPosition().relative(direction, 2),
    content: @Composable BoxScope.(gui: MinecraftComposeGui) -> Unit,
) = MinecraftComposeGui(
    blockWidth, blockHeight,
    content,
    this,
    position
)

/**
 * A server-side gui making use of Compose and Compose UI. You may create this gui using
 * [displayComposable].
 * Internally, the gui is rendered on maps, which are placed inside invisible item frames.
 * Everything only happens through packets, therefore the gui does not really exist on
 * the server.
 * If you want to remove this gui, call the [close] function.
 */
class MinecraftComposeGui(
    val blockWidth: Int, val blockHeight: Int,
    val content: @Composable BoxScope.(gui: MinecraftComposeGui) -> Unit,
    val player: ServerPlayer,
    val position: BlockPos,
) : CoroutineScope {
    companion object {
        private val playerGuis = ConcurrentHashMap<UUID, MinecraftComposeGui>()

        init {
            ServerLifecycleEvents.SERVER_STOPPING.register {
                playerGuis.values.forEach { it.close() }
                playerGuis.clear()
            }

            ServerPlayConnectionEvents.DISCONNECT.register { handler, server ->
                playerGuis[handler.player.uuid]?.close()
            }
        }

        internal fun onSwingHand(player: ServerPlayer, packet: ServerboundSwingPacket) {
            if (packet.hand == InteractionHand.MAIN_HAND) {
                playerGuis[player.uuid]?.onLeftClick()
            }
        }

        internal fun onUpdateSelectedSlot(player: ServerPlayer, packet: ServerboundSetCarriedItemPacket): Boolean {
            val gui = playerGuis[player.uuid] ?: return false

            val slotPair = player.inventory.selected to packet.slot
            val (prevSlot, newSlot) = slotPair

            val scrollDelta = when {
                slotPair == 8 to 0 -> 1f
                slotPair == 0 to 8 -> -1f
                prevSlot < newSlot -> 1f
                prevSlot > newSlot -> -1f
                else -> return false
            }

            return gui.onScroll(scrollDelta * 3)
        }

        private fun Vec3.toMkArray() = mk.ndarray(doubleArrayOf(x, y, z))
        private fun Vec3i.toMkArray() = mk.ndarray(doubleArrayOf(x.toDouble(), y.toDouble(), z.toDouble()))

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

    private class GuiChunk(
        val mapId: Int = MapIdGenerator.nextId(),
        private val colors: ByteArray = ByteArray(128 * 128),
    ) {
        private var dirty = false
        private var startX = 0
        private var startY = 0
        private var endX = 0
        private var endY = 0

        fun setColor(x: Int, y: Int, colorId: Byte) {
            val previousColorId = colors[x + y * 128]
            colors[x + y * 128] = colorId

            if (previousColorId != colorId) {
                if (dirty) {
                    startX = min(startX, x); startY = min(startY, y)
                    endX = max(endX, x); endY = max(endY, y)
                } else {
                    dirty = true
                    startX = x; startY = y
                    endX = x; endY = y
                }
            }
        }

        fun createPacket(): ClientboundMapItemDataPacket? {
            if (!dirty) return null
            dirty = false

            val width = endX + 1 - startX
            val height = endY + 1 - startY
            val packetColors = ByteArray(width * height)

            for (x in 0 until width) {
                for (y in 0 until height) {
                    packetColors[x + y * width] = colors[startX + x + (startY + y) * 128]
                }
            }

            val updateData = MapItemSavedData.MapPatch(startX, startY, width, height, packetColors)
            return ClientboundMapItemDataPacket(mapId, 0, false, null, updateData)
        }
    }

    override val coroutineContext = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

    // values for geometry

    private val guiDirection = player.direction.opposite
    private val placementDirection = guiDirection.getCounterClockWise(Direction.Axis.Y)

    // the perceived position of the display is one behind the actual item frame position
    private val displayPosition = when (guiDirection) {
        Direction.EAST, Direction.SOUTH -> position
        Direction.WEST, Direction.NORTH -> position.relative(guiDirection.opposite)
        else -> position
    }

    private val planePoint = displayPosition.toMkArray()
    private val planeNormal = guiDirection.normal.toMkArray()

    private val topCorner = displayPosition.below(blockHeight - 1).relative(
        placementDirection,
        blockWidth - (if (guiDirection == Direction.WEST || guiDirection == Direction.SOUTH) 0 else 1)
    ).withoutAxis(guiDirection.axis)
    private val bottomCorner = displayPosition.relative(placementDirection.opposite).above().relative(
        placementDirection,
        if (guiDirection == Direction.WEST || guiDirection == Direction.SOUTH) 1 else 0
    ).withoutAxis(guiDirection.axis)

    // values for color mapping

    private val bitmapToMapColorCache = HashMap<Int, Byte>()
    private fun bitmapToMapColor(bitmapColor: Int) = bitmapToMapColorCache.getOrPut(bitmapColor) {
        Color(bitmapColor).run { SRGB(red, green, blue, alpha) }.let { color ->
            when (color.alpha) {
                0f -> MaterialColorUtils.whiteMaterialColorId
                else -> MaterialColorUtils.toMaterialColorId(color)
            }
        }
    }

    // values for rendering

    private val frameDispatcher = FrameDispatcher(coroutineContext) { updateMinecraftMaps() }
    private val scene = ComposeScene(coroutineContext) { frameDispatcher.scheduleFrame() }

    private val guiChunks = Array(blockWidth * blockHeight) { GuiChunk() }
    private fun getGuiChunk(x: Int, y: Int) = guiChunks[x + y * blockWidth]

    private var placedItemFrames = false
    private val itemFrameEntityIds = ArrayList<Int>(blockWidth * blockHeight)

    init {
        scene.setContent {
            Box(Modifier.size((blockWidth * 128).dp, (blockHeight * 128).dp)) {
                content(this@MinecraftComposeGui)
            }
        }

        playerGuis[player.uuid]?.close()
        playerGuis[player.uuid] = this
    }

    private fun updateMinecraftMaps() {
        // TODO maybe don't initialize this for each update
        val bitmap = Bitmap()
        if (!bitmap.allocN32Pixels(blockWidth * 128, blockHeight * 128, true))
            logError("Could not allocate the required resources for rendering the compose gui!")
        val canvas = Canvas(bitmap)

        scene.render(canvas, System.nanoTime())

        val networkHandler = player.connection

        for (xFrame in 0 until blockWidth) {
            for (yFrame in 0 until blockHeight) {
                val guiChunk = getGuiChunk(xFrame, yFrame)
                val mapId = guiChunk.mapId

                for (x in 0 until 128) {
                    for (y in 0 until 128) {
                        guiChunk.setColor(x, y, bitmapToMapColor(bitmap.getColor(xFrame * 128 + x, yFrame * 128 + y)))
                    }
                }

                // send the map data
                val updatePacket = guiChunk.createPacket()
                if (updatePacket != null) networkHandler.send(updatePacket)

                if (!placedItemFrames) {
                    val framePos = position.below(yFrame).relative(placementDirection, xFrame)

                    // spawn the fake item frame
                    val itemFrame = GlowItemFrame(player.level, framePos, guiDirection)
                    itemFrameEntityIds += itemFrame.id
                    itemFrame.isInvisible = true
                    networkHandler.send(itemFrame.addEntityPacket)

                    // put the map in the item frame
                    val composeStack = Items.FILLED_MAP.defaultInstance.apply {
                        orCreateTag.putInt("map", mapId)
                    }
                    itemFrame.setItem(composeStack, false)
                    networkHandler.send(ClientboundSetEntityDataPacket(itemFrame.id, itemFrame.entityData, true))
                }
            }
        }

        if (!placedItemFrames) placedItemFrames = true
    }

    private fun calculateOffset(): Offset? {
        val intersection = rayPlaneIntersection(
            player.eyePosition.toMkArray(),
            player.lookAngle.toMkArray(),
            planePoint,
            planeNormal
        )

        val (worldX, worldY) = intersection.withoutAxis(guiDirection.axis)

        val (topX, topY) = topCorner
        val (bottomX, bottomY) = bottomCorner

        val planeX = when (worldX) {
            in bottomX..topX -> worldX - bottomX
            in topX..bottomX -> bottomX - worldX
            else -> return null
        }

        val planeY = when (worldY) {
            in bottomY..topY -> worldY - bottomY
            in topY..bottomY -> bottomY - worldY
            else -> return null
        }

        return Offset((planeX * 128).toFloat(), (planeY * 128).toFloat())
    }

    private fun onLeftClick() = launch {
        val offset = calculateOffset() ?: return@launch

        // ensure that the following press-release combo is in correct order, therefore release
        scene.sendPointerEvent(PointerEventType.Release, offset)

        scene.sendPointerEvent(PointerEventType.Press, offset)
        scene.sendPointerEvent(PointerEventType.Release, offset)
    }

    private fun onScroll(delta: Float): Boolean {
        val offset = calculateOffset() ?: return false

        // only reset the slot if the player is directly looking at the gui
        player.connection.send(ClientboundSetCarriedItemPacket(4))
        player.inventory.selected = 4

        launch {
            @OptIn(ExperimentalComposeUiApi::class)
            scene.sendPointerEvent(PointerEventType.Scroll, offset, Offset(0f, delta))
        }
        return true
    }

    /**
     * Safely removes this gui. This function will be called automatically
     * if the server shuts down.
     */
    fun close() {
        player.connection.send(ClientboundRemoveEntitiesPacket(*itemFrameEntityIds.toIntArray()))
        MapIdGenerator.makeOldIdAvailable(guiChunks.map { it.mapId })
        coroutineContext.close()
        scene.close()
        playerGuis.remove(player.uuid, this)
    }
}
