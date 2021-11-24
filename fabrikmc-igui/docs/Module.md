# Module fabrikmc-igui

The Inventory GUI (`igui` in short) provides a high level *server-side* GUI API. You build the GUI using a custom and
easy-to-use builder DSL.

The GUI rerenders automatically if the state changes, it provides animations and "compounds" which are an easy-to-use
abstraction for listing a lot of elements of the same type.

${dependencyNotice}

## Using the igui builder

### Create a new gui

The [igui][net.axay.fabrik.igui.igui] function opens up a new gui builder. Use
the [page][net.axay.fabrik.igui.GuiBuilder.page] function to add a new page to this gui, you can that function as often
as you want.

```kotlin
igui(GuiType.NINE_BY_SIX, "My cool gui".literal, defaultPageKey = 1) {
    page(1) {
        // access to the page builder inside here
    }
}
```

### The page builder

The [page builder][net.axay.fabrik.igui.GuiBuilder.PageBuilder] is the most important part of the gui dsl. Inside this
builder, you have access [all functions adding elements to the gui][net.axay.fabrik.igui.GuiBuilder.PageBuilder].

# Package net.axay.fabrik.igui

Contains the GUI builder and all GUI implementation classes

# Package net.axay.fabrik.igui.elements

GUI elements can be rendered by a GUI implementation

# Package net.axay.fabrik.igui.events

GUI events are passed to the user of this API for usage in callbacks

# Package net.axay.fabrik.igui.observable

Observable lists and properties which are used by GUI elements to handle non-static content
