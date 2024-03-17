package com.github.silhouettemc.listener.player

import com.github.silhouettemc.Silhouette
import com.github.silhouettemc.punishment.PunishmentType
import com.github.silhouettemc.util.ConfigUtil
import com.github.silhouettemc.util.text.translate
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent

object PlayerLoginListener: Listener {

    @EventHandler
    fun AsyncPlayerPreLoginEvent.onPreLogin() {
        // todo: async
        val existingPunishment = Silhouette.getInstance().database.getLatestActivePunishment(uniqueId, PunishmentType.BAN)
            ?: return

        val expiration = existingPunishment.expiration

        val placeholders = mapOf(
            "punisher" to existingPunishment.punisher.getReadableName(),
            "reason" to (existingPunishment.reason ?: "No reason specified"),
            "expiry" to (expiration?.toString() ?: "Never")
        )
        val msg = ConfigUtil.getMessage("banScreen", placeholders)

        disallow(
            AsyncPlayerPreLoginEvent.Result.KICK_BANNED,
            translate(msg)
        )
    }
}