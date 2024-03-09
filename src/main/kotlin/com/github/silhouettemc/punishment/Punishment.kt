package com.github.silhouettemc.punishment

import com.github.silhouettemc.Silhouette
import com.github.silhouettemc.Silhouette.Companion.mm
import com.github.silhouettemc.actor.Actor
import com.github.silhouettemc.util.translate
import com.github.silhouettemc.util.type.ReasonContext
import com.j256.ormlite.field.DataType
import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import org.bukkit.Bukkit
import java.util.*

@DatabaseTable(tableName = "punishments")
open class Punishment(
    @DatabaseField(canBeNull = false)
    val player: UUID,
    @DatabaseField(canBeNull = false, dataType = DataType.SERIALIZABLE)
    val punisher: Actor,
    @DatabaseField(canBeNull = true)
    val reason: String? = null,
    @DatabaseField(canBeNull = false)
    val type: PunishmentType,

    @DatabaseField(canBeNull = false)
    val id: UUID = UUID.randomUUID(),

    @DatabaseField(canBeNull = false)
    val punishedOn: Date = Date()
) {

    fun process(reason: ReasonContext) {
        Silhouette.getInstance().database.addPunishment(this) // todo: async

        if (type.shouldDisconnect) handleDisconnect()
        if (!reason.isSilent) broadcastPunishment()
    }

    private fun handleDisconnect() {
        val player = Bukkit.getPlayer(player) ?: return

        if (reason == null) {
            player.kick(mm.deserialize("You have been ${type.punishedName}"))
            return
        }

        player.kick(
            mm.deserialize(
                """
                    You have been ${type.punishedName}
                    Reason: $reason
                """.trimIndent()
            )
        )
    }

    private fun broadcastPunishment() {
        Bukkit.broadcast(translate(
            """
                <p>$player was <s>${type.punishedName}</s> by <s>${punisher.getReadableName()}</s>
            """.trimIndent()
        ))
    }

}
