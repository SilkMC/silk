# Module fabrikmc-commands

A command DSL, built on top of Brigardier - the idea of this DSL is to bring the Kotlin builder style to Brigardier.
This DSL does not wrap Brigardier, it just extends it.

${dependencyNotice}

## Creating a command

You can open a new command builder using the `command` function:

```kt
command("mycommand") {
    // the command builder
}
```

For a command which should be handled by the client, use the `clientCommand` function instead.

## Registering the command

This command will be registered automatically if it is initialized at the correct moment (during initialization of your
mod).

If you need to register it manually, call the `setupRegistrationCallback()` (server-side) or `register()` (client-side)
functions by yourself.

# Package net.axay.fabrik.commands

Contains all command builder extension functions

# Package net.axay.fabrik.commands.registration

Utilities for registering commands
