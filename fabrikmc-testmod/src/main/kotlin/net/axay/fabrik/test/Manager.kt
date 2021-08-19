package net.axay.fabrik.test

import net.axay.fabrik.test.commands.guiCommand
import net.axay.fabrik.test.commands.persistenceTestCommand
import net.axay.fabrik.test.commands.sideboardCommand
import net.axay.fabrik.test.commands.textTestCommand

fun init() {
    guiCommand
    persistenceTestCommand
    textTestCommand
    sideboardCommand
}
