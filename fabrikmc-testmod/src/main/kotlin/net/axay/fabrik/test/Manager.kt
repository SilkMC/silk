package net.axay.fabrik.test

import net.axay.fabrik.core.Fabrik
import net.axay.fabrik.test.gui.GuiCommand
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents

fun init() {

    ServerLifecycleEvents.SERVER_STARTING.register {
        println("server starting?")
    }

    ServerLifecycleEvents.SERVER_STARTED.register {
        println("server started!?")
    }

    Fabrik.init()

    GuiCommand

}
