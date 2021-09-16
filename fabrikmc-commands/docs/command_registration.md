# Package net.axay.fabrik.commands.registration

## Command registration

Commands created using the [net.axay.fabrik.commands.command] builder function **will be registered automatically** if called during initialization of your
mod (only if `register` is set to true, which is the default).

If you need to register it manually, call the `setupRegistrationCallback()` (server-side) or `register()` (client-side)
function by yourself.

*usage example:*

```kt
// not needed by default, but in this case we set register to false
val myCommand = command("mycommand", register = false) {
    runs { }
}
// on the server-side this has to be done using a callback
myCommand.setupRegistrationCallback()
// on the client-side this can be done immediately
myCommand.register()
```
