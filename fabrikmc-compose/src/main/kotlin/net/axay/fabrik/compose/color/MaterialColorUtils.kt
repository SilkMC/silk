package net.axay.fabrik.compose.color

import androidx.compose.ui.graphics.Color
import com.github.ajalt.colormath.calculate.differenceCIE2000
import com.github.ajalt.colormath.model.LAB
import com.github.ajalt.colormath.model.RGB
import com.github.ajalt.colormath.model.RGBInt
import com.github.ajalt.colormath.transform.interpolate
import com.github.ajalt.colormath.transform.multiplyAlpha
import net.axay.fabrik.compose.mixin.MaterialColorAccessor
import net.axay.fabrik.core.logging.logWarning
import net.minecraft.world.level.material.MaterialColor

/**
 * Utilities for working with [MaterialColor].
 */
object MaterialColorUtils {

    private val white = RGB(1f, 1f, 1f)

    /**
     * The [MaterialColor] id of pure white.
     */
    val whiteMaterialColorId = (MaterialColor.SNOW.id * 4 + 2).toByte()

    /**
     * A list of colors in the [LAB] representation mapped to their pre-calculated
     * [MaterialColor] id. (Pre-calculated means that they have been multiplied by 4 and
     * their shade has been added to the id.)
     */
    private val materialColorIds = ArrayList<Pair<LAB, Byte>>().apply {
        MaterialColorAccessor.getMaterialColors()
            .filter { it != null && it.col != 0 }
            .forEach { materialColor ->
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

                    val realColor = RGBInt(materialColor.col.toUInt()).toSRGB()
                        .run { RGB(r, g, b, alphaFromId / 255f) }
                        .multiplyAlpha()
                        .toLAB()
                    this += realColor to (materialColor.id * 4 + it).toByte()
                }
            }
    }.toTypedArray()

    /**
     * Same as [materialColorIds], but without white. This is used by the [toMaterialColorId] function.
     */
    private val materialColorIdsNoWhite = materialColorIds.filterNot { it.second == whiteMaterialColorId }.toTypedArray()

    /**
     * Scales the given [color] down to a [MaterialColor] using
     * [differenceCIE2000] and returns the id of that material color.
     */
    fun toMaterialColorId(color: com.github.ajalt.colormath.Color): Byte {
        val interpolatedColor = color.run {
            if (alpha < 1f) white.interpolate(this, alpha) else this
        }

        if (interpolatedColor.isNearlyWhite) return whiteMaterialColorId

        return materialColorIdsNoWhite.minByOrNull { it.first.differenceCIE2000(interpolatedColor) }!!.second
    }

    private val com.github.ajalt.colormath.Color.isNearlyWhite get() = differenceCIE2000(white) <= 2.5f
}

/**
 * Converts the non-shaded color value of this [MaterialColor] instance to
 * the Compose [Color] representation.
 */
fun MaterialColor.toCompose() = RGBInt(col.toUInt()).run { Color(r.toInt(), g.toInt(), b.toInt()) }
