# Module fabrikmc-commands

A command DSL, built on top of Brigardier - the idea of this DSL is to bring the Kotlin builder style to Brigardier.
This DSL does not hide vanilla Brigardier from you.

${dependencyNotice}

## Usage

You first have to create a command instance using the [command][net.axay.fabrik.commands.command]
or [clientCommand][net.axay.fabrik.commands.clientCommand] function. <br>
Have a look at the `commands` package below for more information.

After that, you can register that command instance. <br>
Have a look at the `commands.registration` package below to see if you have to do it manually, and if yes, how.
