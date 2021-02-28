package net.axay.fabrik.test

import net.axay.fabrik.commands.setupRegistrationCallback
import net.axay.fabrik.core.Fabrik
import net.axay.fabrik.test.commands.guiCommand
import net.axay.fabrik.test.commands.textTestCommand

fun init() {

    Fabrik.init()

    guiCommand.setupRegistrationCallback()
    textTestCommand.setupRegistrationCallback()

}
