# Package net.silkmc.silk.game.sideboard

A sideboard builder (displayed using scoreboards), supporting boards with changing content. The sideboard can be
displayed to a selection of players.

## Sideboard management

### Creating a sideboard

Use the top-level [sideboard][et.silkmc.silk.game.sideboard.sideboard] function.

```kotlin
val mySideboard = sideboard("My Sideboard".literal) {
    // build your sideboard here
}
```

### Display sideboard to a player

To show the sideboard to a player, you can simply use the
[displayToPlayer][net.silkmc.silk.game.sideboard.Sideboard.displayToPlayer] function.

```kotlin
mySideboard.displayToPlayer(player)
```

To remove a sideboard for a player, just call
[hideFromPlayer][net.silkmc.silk.game.sideboard.Sideboard.hideFromPlayer].

## Sideboard builder

### Basic text line

```kotlin
val mySideboard = sideboard("My Sideboard".literal) {
    line("Hey, this is".literal)
    line("my cool board!".literal)
    emptyLine()
    line(literalText("colors work as well!") {
        color = 0xFF9658
    })
}
```

See [line][net.silkmc.silk.game.sideboard.SideboardBuilder.line] and
[emptyLine][net.silkmc.silk.game.sideboard.SideboardBuilder.emptyLine].

### Updatable line

You can create an [updatable line][net.silkmc.silk.game.scoreboard.ScoreboardLine.Updatable] as a normal value everywhere

```kotlin
val updatableLine = ScoreboardLine.Updatable()
// or optionally pass an initial value
val updatableLine = ScoreboardLine.Updatable(initial = "Nothing here".literal)
```

and then use it in a sideboard builder

```kotlin
val mySideboard = sideboard("My Sideboard".literal) {
    line("The following line".literal)
    line("is updatable:".literal)
    line(updatableLine)
}
```

To change the value of that updatable line, just call either
[launchUpdate][net.silkmc.silk.game.scoreboard.ScoreboardLine.Updatable.launchUpdate]
or if you are already in a suspending context just call
[update][net.silkmc.silk.game.scoreboard.ScoreboardLine.Updatable.update].

```kotlin
updatableLine.update("New line content!".literal)
```

### Periodically updating line

If you want to update a line periodically, you can easily do so using the
[updatingLine][net.silkmc.silk.game.sideboard.SideboardBuilder.updatingLine] function of the sideboard builder.

```kotlin
val mySideboard = sideboard("My Sideboard".literal) {
    updatingLine(1.seconds) {
        literalText("changing color") {
            color = (0x000000..0xFFFFFF).random()
        }
    }
}
```
