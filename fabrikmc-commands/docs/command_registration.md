# Package net.axay.fabrik.commands.registration

Utilities for command registration

## Command registration

### Automatic registration (Recommended)

Commands created using the [command][net.axay.fabrik.commands.command]
or [clientCommand][net.axay.fabrik.commands.clientCommand] builder function **will be registered automatically** if
called during initialization of your mod.

Note: this is only the case if the `register` parameter is set to `true`, which is the default

#### Example for automatic registration

First create the command somewhere

```kotlin
val myCommand = command("mycommand") { }
```

If this value will be initialized during the startup phase of your mod, you are done.

*Otherwise*, you can register the command by mentioning it in the entrypoint of your mod

```kotlin
fun init() {
    myCommand // not needed, if the value is initialized anyways
}
```

### Manual registration

If you need to register it manually, call
the [setupRegistrationCallback][net.axay.fabrik.commands.registration.setupRegistrationCallback] (server-side)
or [register][net.axay.fabrik.commands.registration.register] (client-side) function by yourself.

#### Example for manual registration

First create the command somewhere

```kotlin
// note that register is set to false
val myCommand = command("mycommand", register = false) { }
```

After that, you can register the command by manually calling the registration function

```kotlin
fun init() {
    myCommand.setupRegistrationCallback()
}
```
