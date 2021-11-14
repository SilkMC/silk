@file:Suppress("FunctionName")

package net.axay.fabrik.compose.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import net.axay.fabrik.compose.MinecraftComposeGui

/**
 * Adds an opinionated window header suitable for [MinecraftComposeGui]s. This
 * header provides a cross for closing the [gui].
 * You may use this in a [Column], otherwise it will overlay with other components.
 *
 * @param gui the gui which should be closed if the player uses the top-right cross
 * @param title the current title of the gui
 */
@Composable
fun McWindowHeader(gui: MinecraftComposeGui, title: String = "") {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(title, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 5.dp))
        Box(
            modifier = Modifier
                .size(30.dp)
                .padding(3.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(207, 226, 233))
                .clickable {
                    gui.close()
                }
        ) {
            Icon(Icons.Default.Close, "Close Gui", Modifier.align(Alignment.Center))
        }
    }
}
