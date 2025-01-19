package net.silkmc.silk.paper.events.internal

import io.papermc.paper.adventure.AdventureComponent
import net.minecraft.core.Holder
import net.minecraft.world.damagesource.DamageEffects
import net.minecraft.world.damagesource.DamageScaling
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.phys.Vec3
import net.silkmc.silk.core.event.EventScopeProperty
import net.silkmc.silk.core.event.PlayerEvents
import net.silkmc.silk.paper.conversions.mcEntity
import net.silkmc.silk.paper.conversions.mcPlayer
import net.silkmc.silk.paper.events.listenSilk
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerLoginEvent
import org.bukkit.event.player.PlayerQuitEvent

fun PlayerEvents.setupPaper() {
    listenSilk<PlayerLoginEvent> {
        preLogin.invoke(PlayerEvents.PlayerEvent(it.player.mcPlayer))
    }
    listenSilk<PlayerJoinEvent> {
        postLogin.invoke(PlayerEvents.PlayerEvent(it.player.mcPlayer))
    }
    listenSilk<PlayerQuitEvent> {
        preQuit.invoke(PlayerEvents.PlayerQuitEvent(it.player.mcPlayer, AdventureComponent(it.quitMessage())))
    }

    listenSilk<PlayerDeathEvent> {
        val pos = it.player.location.toVector()
        onDeath.invoke(
            PlayerEvents.PlayerDeathEvent(
                it.player.mcPlayer, DamageSource(
                    Holder.direct(
                        DamageType(
                            it.damageSource.damageType.key.key,
                            DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER, //TODO: Use DamageTypeRegistryEntry in 1.21.4
                            it.damageSource.foodExhaustion,
                            DamageEffects.valueOf(it.damageSource.damageType.damageEffect.toString())
                        )
                    ),
                    it.entity.mcEntity,
                    it.entity.mcEntity,
                    Vec3(pos.x, pos.y, pos.z),
                ), EventScopeProperty(AdventureComponent(it.deathMessage()))
            )
        )
    }
}
