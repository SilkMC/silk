# Package net.axay.fabrik.commands

Contains all command builder functions

## The command builder DSL

You can also [watch the YouTube Tutorial](https://youtu.be/KPAw8mxwskQ).

### Create a new command

You can create a new command using the [command][net.axay.fabrik.commands.command]
or [clientCommand][net.axay.fabrik.commands.clientCommand] function.

```kt
command("mycommand") {
    // the command builder body
}
```

### Define execution logic

You can use the [runs][net.axay.fabrik.commands.CommandBuilder.runs] function.

```kt
command("mycommand") {
    runs {
        // inside the command handler
    }
}
```

[runsAsync][net.axay.fabrik.commands.CommandBuilder.runsAsync] is an alternative for `runs` and launches the command
handler in an async coroutine.

#### The command execution context

The `CommandContext` is passed in to runs as `this`. The most useful value of the context is `source`, which allows you
to get information about the caller of the command.

```kt
runs {
    val caller = source.player
}
```

Note: the above snippet also validates if it really was a player who called this command, you don't have to do this
manually

### Add subcommands (literals) to the command

Subcommands can be easily added using the [literal][net.axay.fabrik.commands.CommandBuilder.literal] function.

```
command("mycommand") {
    literal("subcommand") {
        // the subcommand builder body
    }
}
```

#### Even more nesting

Maybe you now start to see the concept here, all builder functions inside the command builder can be infinitely nested:

```kt
command("mycommand") {
    literal("subcommand") {
        literal("megasubcommand") {
            // the subcommand builder body
        }
    }
}
```

#### Using the runs function for subcommands

You can use the runs function in the same way is it is done with the root command (see above). Additionally, a shorter
syntax making use of infix functions is also available to you.

```kt
literal("subcommand") {
    runs { }
}

// OR even shorter (if you don't need anything else other than runs)

literal("subcommand") runs { }
```

### Add arguments

Arguments can be added using one of the [argument][net.axay.fabrik.commands.CommandBuilder.argument] functions.

#### Define an argument

For all arguments you have to specify the argument name, which will be displayed to the player as a tooltip above the
command prompt if it is not blocked by suggestions.

##### Reified argument type

For all [Kotlin basic types](https://kotlinlang.org/docs/basic-types.html) (except arrays) and a few common Minecraft
classes (e.g. [Identifier][net.minecraft.util.Identifier]) the argument types are predefined, and you can just pass them
as `reified T` to the `argument` function.

```kt
argument<String>("myargument")
```

##### Brigadier argument types

This variant of the argument function allows you to specify the argument type in the classical Brigadier way.

```kt
argument("myargument", StringArgumentType.string())
```

##### Custom parser, which deserializes the string to your type

If there exists no argument type for your use case, you could implement a custom parser, which converts the user input
to the given type `T`

```kt
argument("testarg", { it.readString() })
```

#### Using the runs function for arguments

You can use the runs function in the same way is it is done with the root command (see above).

##### Get the argument value inside `runs`

The recommended way to resolve the value of the argument is the following:

```kt
argument<String>("mystringarg") { stringArg ->
    runs {
        source.player.sendText(stringArg())
    }
}
```

So, what is happening here? You are getting a function passed in by
the [argument][net.axay.fabrik.commands.CommandBuilder.argument] function as `it`, but you can rename it to whatever you
want (in this case "stringArg").

This "stringArg" function is only callable in the context of [runs][net.axay.fabrik.commands.CommandBuilder.runs]
or [runsAsync][net.axay.fabrik.commands.CommandBuilder.runsAsync], where it will return the current value of the
argument, entered by the user.

### Suggestions

#### For subcommands

Subcommands will be suggested automatically.

#### Add argument value suggestions

You can use one the many suggest functions to suggest argument values:
[suggestSingle][net.axay.fabrik.commands.ArgumentCommandBuilder.suggestSingle]
, [suggestSingleWithTooltip][net.axay.fabrik.commands.ArgumentCommandBuilder.suggestSingleWithTooltip]
, [suggestSingleSuspending][net.axay.fabrik.commands.ArgumentCommandBuilder.suggestSingleSuspending]
, [suggestSingleWithTooltipSuspending][net.axay.fabrik.commands.ArgumentCommandBuilder.suggestSingleWithTooltipSuspending]
, [suggestList][net.axay.fabrik.commands.ArgumentCommandBuilder.suggestList]
, [suggestListWithTooltips][net.axay.fabrik.commands.ArgumentCommandBuilder.suggestListWithTooltips]
, [suggestListSuspending][net.axay.fabrik.commands.ArgumentCommandBuilder.suggestListSuspending]
, [suggestListWithTooltipsSuspending][net.axay.fabrik.commands.ArgumentCommandBuilder.suggestListWithTooltipsSuspending]

An example would be:

```kt
argument<String>("mystringargument") {
    suggestList { listOf("Tom", "Apple", "Tiny Potato") }
}
```
