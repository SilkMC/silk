package net.axay.fabrik.core

import net.axay.fabrik.core.task.FabrikCoroutineManager

object Fabrik {

    /**
     * You should call this function if you intend to
     * use the Fabrik API.
     */
    fun init() {

        FabrikCoroutineManager.init()

    }

}
