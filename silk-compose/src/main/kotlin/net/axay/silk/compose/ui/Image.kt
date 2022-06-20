package net.silkmc.silk.compose.ui

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import net.silkmc.silk.compose.icons.McIcon
import net.silkmc.silk.compose.internal.AssetsLoader

/**
 * Renders the icon of any [Item] to this gui as an [Image].
 *
 * See [Image] for a description of the parameters.
 *
 * @see Image
 */
@Composable
fun McImage(
    icon: McIcon,
    contentDescription: String? = icon,
    modifier: Modifier = Modifier,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
) {
    val image by produceState<ImageBitmap?>(null, key1 = icon) {
        value = AssetsLoader.loadImage(icon)
    }

    if (image != null) {
        val painter = remember { BitmapPainter(image!!, filterQuality = FilterQuality.None) }

        Image(
            painter = painter,
            contentDescription = contentDescription,
            modifier = modifier,
            alignment = alignment,
            contentScale = contentScale
        )
    }
}
