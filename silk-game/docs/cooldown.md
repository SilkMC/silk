# Package net.silkmc.silk.game.cooldown

Functions for handling cooldown for actions performed on or by entities, including players

## Cooldown management

### Create a cooldown "key"

Each *type* of cooldown can be represented by a new instance of [Cooldown][net.silkmc.silk.game.Cooldown].

```kotlin
val myCooldown = Cooldown()
```

You can also pass an [Identifier][net.minecraft.util.Identifier] to the `Cooldown` constructor.

#### Specify a default cooldown length

Optionally, you can specify a default cooldown length, because the `defaultLength` has a default value of `50L` which
you probably don't want. This step is irrelevant for you, if you are specifying the length of the cooldown explicitly
each time.

```kotlin
val myCooldown = Cooldown(defaultLength = 1000L)
```

The time unit is `ms`.

### Cooldown actions

#### hasCooldown

You can check whether a player has a cooldown or not using
the [hasCooldown][net.silkmc.silk.game.cooldown.Cooldown.hasCooldown] function.

```kotlin
if (myCooldown.hasCooldown(entity).not()) {
    // no cooldown
}
```

#### applyCooldown

You can apply a cooldown instance using the [applyCooldown][net.silkmc.silk.game.cooldown.Cooldown.applyCooldown]
function.

```kotlin
myCooldown.applyCooldown(entity, delay = 2000L)

// the following will be true now
myCooldown.hasCooldown(entity)
```

Note: This function also takes a `delay` parameter, but that one is not required.

#### withCooldown

Often you want to execute some code if the player or entity does not have an active cooldown, and then apply the
cooldown. This can be done in a short form using the [withCooldown][net.silkmc.silk.game.cooldown.Cooldown.withCooldown]
function.

```kotlin
myCooldown.withCooldown(entity, delay = 2000L) {
    // this will be executed at a maximum of 1 time per 2 seconds
    logInfo("hey")
}
```
