# Module fabrikmc-compose

Adds server-side Compose support to the game. This allows you to create any gui you want, without any limitations -
using a modern UI toolkit.

${dependencyNotice}

The Compose dependency is bundled with fabrikmc-compose, you don't have to include it yourself.

## Usage

You can open a server-side compose gui and show it to a player using
the [player.displayComposable][net.axay.fabrik.compose.displayComposable] function:

```kotlin
player.displayComposable(8, 6) {
    YourComposableFunction()
}

\\@Composable
fun YourComposableFunction() {
    Button(
        onClick = { logInfo("clicked button") }
    ) {
        Text("Click me")
    }
}
```

and that's it, you now have access to the world of Compose in Minecraft.

You can now have a look at the **UI** package (below) to see custom composable functions provided by fabrikmc-compose.

## Compose docs

There are docs at [Android Developer Website](https://developer.android.com/jetpack/compose/documentation) and you can
find desktop specific documentation at the [compose-jb](https://github.com/JetBrains/compose-jb) repository.
Additionally, there is a  [communtiy maintained playground](https://foso.github.io/Jetpack-Compose-Playground/).

# Package net.axay.fabrik.compose

Implementation of Compose scenes for Minecraft

# Package net.axay.fabrik.compose.color

Color utilities for working with map colors

# Package net.axay.fabrik.compose.icons

Contains generated constants for Minecraft icons

# Package net.axay.fabrik.compose.ui

UI components (composable functions) useful for Minecraft guis
