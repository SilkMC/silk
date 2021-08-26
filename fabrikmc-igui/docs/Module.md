# Module fabrikmc-igui

The Inventory GUI (`igui` in short) provides a high level **server-side** GUI API. You build the GUI using a
custom and easy-to-use builder DSL.

The GUI rerenders automatically if the state changes, it provides animations and "compounds" which are
an easy-to-use abstraction for listing a lot of elements of the same type.

${dependencyNotice}

# Package net.axay.fabrik.igui

Contains the GUI builder and all GUI implementation classes

# Package net.axay.fabrik.igui.elements

GUI elements can be rendered by a GUI implementation

# Package net.axay.fabrik.igui.events

GUI events are passed to the user of this API for usage in callbacks

# Package net.axay.fabrik.igui.observable

Observable lists and properties which are used by GUI elements to handle non-static content
