# Package net.silkmc.silk.game.tablist

A tablist builder supporting changing headers/footers and also player name editing. 

## Tablist management

### Creating a tablist

Use the top-level [tablist][net.silkmc.silk.game.tablist.tablist] function.

```kotlin
val myTablist = tablist() {
    // build your tablist here
}
```

### Updating player names

```kotlin
// update and regenerate all names
myTablist.updateNames()

// update and regenerate the name of this player
val player = Silk.currentServer?.playerList?.getPlayerByName("btwonion") ?: return
myTablist.updateName(player)
```


## Tablist Builder

```kotlin
val myTablist = tablist() {

    generateName {
        // put your name generator here
        // it provides a ServerPlayer and requires a Component and a String which defines the priority
        
        //Example: 
        literalText("${this.name.string} test") {
            color = (0x44212..0x45641).random()
        } to "2"
    }

    // sets your footers with ScoreboardLines
    footer(
        listOf(ScoreboardLine.UpdatingPeriodically(1.seconds) {
            literalText(UUID.randomUUID().toString()) {
                color = 0x568F97
            }
        })
    )

    // sets your headers with ScoreboardLines
    header(
        listOf(
            ScoreboardLine.Static(literalText("Some static text") { color = 0x200D97 })
        )
    )
    
}
```
You can find all possible lines here: [ScoreboardLine][net.silkmc.silk.game.scoreboard.ScoreboardLine] 