package net.axay.fabrik.compose.color

import androidx.compose.ui.graphics.Color
import com.github.ajalt.colormath.calculate.differenceCIE2000
import com.github.ajalt.colormath.model.LAB
import com.github.ajalt.colormath.model.RGB
import com.github.ajalt.colormath.model.RGBInt
import com.github.ajalt.colormath.transform.interpolate
import com.github.ajalt.colormath.transform.multiplyAlpha
import net.axay.fabrik.core.logging.logWarning
import net.minecraft.block.MapColor

/**
 * Utilities for working with [MapColor].
 */
object MapColorUtils {
    private val white = RGB(1f, 1f, 1f)

    /**
     * The [MapColor] id of pure white.
     */
    val whiteMapColorId = (MapColor.WHITE.id * 4 + 2).toByte()

    /**
     * A list of colors in the [LAB] representation mapped to their pre-calculated
     * [MapColor] id. (Pre-calculated means that they have been multiplied by 4 and
     * their shade has been added to the id.)
     */
    private val mapColorIds = ArrayList<Pair<LAB, Byte>>().apply {
        MapColor.COLORS
            .filter { it != null && it.color != 0 }
            .forEach { mapColor ->
                repeat(4) {
                    val alphaFromId = when (it) {
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
                        .run { RGB(r, g, b, alphaFromId / 255f) }
                        .multiplyAlpha()
                        .toLAB()
                    this += realColor to (mapColor.id * 4 + it).toByte()
                }
            }
    }.toTypedArray()

    /**
     * Same as [mapColorIds], but without white. This is used by the [toMapColorId] function.
     */
    private val mapColorIdsNoWhite = mapColorIds.filterNot { it.second == whiteMapColorId }.toTypedArray()

    /**
     * Scales the given [color] down to a [MapColor] using
     * [differenceCIE2000] and returns the id of that map color.
     */
    fun toMapColorId(color: com.github.ajalt.colormath.Color): Byte {
        val interpolatedColor = color.run {
            if (alpha < 1f) white.interpolate(this, alpha) else this
        }

        if (interpolatedColor.isNearlyWhite) return whiteMapColorId

        return mapColorIdsNoWhite.minByOrNull { it.first.differenceCIE2000(interpolatedColor) }!!.second
    }

    private val com.github.ajalt.colormath.Color.isNearlyWhite get() = differenceCIE2000(white) <= 2.5f
}

/**
 * Converts the non-shaded color value of this map color instance to
 * the Compose [Color] representation.
 */
fun MapColor.toCompose() = RGBInt(color.toUInt()).run { Color(r.toInt(), g.toInt(), b.toInt()) }
