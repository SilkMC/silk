package net.axay.fabrik.compose.ui

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
import net.axay.fabrik.compose.internal.AssetsLoader
import net.minecraft.item.Item

@Composable
fun McImage(
    item: Item,
    modifier: Modifier = Modifier,
    contentDescription: String? = item.name.string,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
) {
    val image by produceState<ImageBitmap?>(null) {
        value = AssetsLoader.loadImage(item)
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
