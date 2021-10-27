package net.axay.fabrik.compose.color

import androidx.compose.ui.graphics.Color
import com.github.ajalt.colormath.calculate.differenceCIE2000
import com.github.ajalt.colormath.model.LAB
import com.github.ajalt.colormath.model.RGB
import com.github.ajalt.colormath.model.RGBInt
import com.github.ajalt.colormath.transform.multiplyAlpha
import net.axay.fabrik.core.logging.logWarning
import net.minecraft.block.MapColor

/**
 * Utilities for working with [MapColor].
 */
object MapColorUtils {
    /**
     * The [MapColor] id of pure white.
     */
    val whiteMapColorId = (MapColor.WHITE.id * 4 + 2).toByte()

    /**
     * A list of colors in the [LAB] representation mapped to their pre-calculated
     * [MapColor] id. (Pre-calculated means that they have been multiplied by 4 and
     * their shade has been added to the id.)
     */
    val mapColorIds = ArrayList<Pair<LAB, Byte>>().apply {
        MapColor.COLORS
            .filter { it != null && it.color != 0 }
            .forEach { mapColor ->
                repeat(4) {
                    val alpha = when (it) {
                        0 -> 180
                        1 -> 220
                        2 -> 255
                        3 -> 135
                        else -> {
                            logWarning("Unsupported color shade: $it - Will use alpha of 255 as a fallback")
                            255
                        }
                    }
                    val realColor = RGBInt(mapColor.color.toUInt()).toSRGB()
                        .run { RGB(r, g, b, alpha / 255f) }
                        .multiplyAlpha()
                    this += realColor.toLAB() to (mapColor.id * 4 + it).toByte()
                }
            }
    }.toTypedArray()

    /**
     * Scales the given [color] down to a [MapColor] using
     * [differenceCIE2000] and returns the id of that map color.
     */
    fun toMapColorId(color: com.github.ajalt.colormath.Color): Byte {
        val multipliedColor = color.multiplyAlpha()
        return mapColorIds.minByOrNull { it.first.differenceCIE2000(multipliedColor) }!!.second
    }
}

/**
 * Converts the non-shaded color value of this map color instance to
 * the Compose [Color] representation.
 */
fun MapColor.toCompose() = Color(color)
