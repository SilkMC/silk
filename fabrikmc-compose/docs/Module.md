# Module fabrikmc-compose

Adds server-side Compose support to the game. This allows you to create any gui you want, without any limitations -
using a modern UI toolkit.

${dependencyNotice}

## Usage

You can open a server-side compose gui and show it to player using the [net.axay.fabrik.compose.displayComposable]
function:

```kotlin
source.player.displayComposable(8, 6) {
    YourComposableFunction()
}
```

And that's it, you now have acces to the world of Compose in Minecraft.

You can now have a look at the **UI** package (below) to see custom composable functions provided by
fabrikmc-compose.

# Package net.axay.fabrik.compose

Implementation of Compose scenes for Minecraft

# Package net.axay.fabrik.compose.color

Color utilities for working with map colors

# Package net.axay.fabrik.compose.ui

UI components (composable functions) useful for Minecraft guis
