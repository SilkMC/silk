package net.silkmc.silk.paper.events.internal

import io.papermc.paper.adventure.AdventureComponent
import io.papermc.paper.adventure.PaperAdventure
import io.papermc.paper.event.player.AsyncChatEvent
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import net.minecraft.network.chat.*
import net.silkmc.silk.core.event.EventScopeProperty
import net.silkmc.silk.core.event.PlayerEvents
import net.silkmc.silk.paper.conversions.mcDamageSource
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
        val event = PlayerEvents.PostLoginEvent(
            it.player.mcPlayer, EventScopeProperty(
                AdventureComponent(it.joinMessage()).deepConverted()
            )
        )
        postLogin.invoke(event)
        it.joinMessage(PaperAdventure.asAdventure(event.joinMessage.get()))
    }
    listenSilk<PlayerQuitEvent> {
        preQuit.invoke(PlayerEvents.PlayerQuitEvent(it.player.mcPlayer, AdventureComponent(it.quitMessage())))
    }

    listenSilk<PlayerDeathEvent> {
        val event = PlayerEvents.PlayerDeathEvent(
            it.player.mcPlayer,
            @Suppress("UnstableApiUsage")
            it.damageSource.mcDamageSource,
            EventScopeProperty(AdventureComponent(it.deathMessage()))
        )
        onDeath.invoke(event)
        it.deathMessage(PaperAdventure.asAdventure(event.deathMessage.get()))
    }

    listenSilk<AsyncChatEvent> { event ->
        val message = event.player.mcPlayer.chatSession?.let { session ->
            val link = SignedMessageLink.root(event.player.uniqueId, session.sessionId)
            val signature = MessageSignature(
                event.signedMessage().signature()?.bytes() ?: byteArrayOf()
            )
            val body = SignedMessageBody(
                event.signedMessage().message(),
                event.signedMessage().timestamp(),
                event.signedMessage().salt(),
                LastSeenMessages.EMPTY
            )
            val unsigned: Component = AdventureComponent(event.signedMessage().unsignedContent())
            val filter: FilterMask = FilterMask.PASS_THROUGH
            PlayerChatMessage(
                link,
                signature,
                body,
                unsigned,
                filter
            )
        } ?: PlayerChatMessage.unsigned(
            event.player.uniqueId,
            PlainTextComponentSerializer.plainText().serialize(event.message())
        )

        val bound: ChatType.Bound = ChatType.bind(ChatType.CHAT, event.player.mcEntity)
        val chatEvent = PlayerEvents.PlayerChatEvent(
            event.player.mcPlayer,
            message,
            bound
        )

        PlayerEvents.onChat.invoke(chatEvent)
        event.isCancelled = chatEvent.isCancelled.get()
        event.message(PaperAdventure.asAdventure(chatEvent.message.decoratedContent()))
    }
}
