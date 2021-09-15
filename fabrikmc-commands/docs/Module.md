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

You can see all command builder function here: [net.axay.fabrik.commands]

### Clientside

For a command which should be handled by the client, use the `clientCommand` function instead.

## Registering the command

Commands created using the `command` builder function will be registered automatically if called during initialization of your
mod (only if `register` is set to true).

If you need to register it manually, call the `setupRegistrationCallback()` (server-side) or `register()` (client-side)
function by yourself.

# Package net.axay.fabrik.commands

Contains all command builder extension functions

# Package net.axay.fabrik.commands.registration

Utilities for registering commands
