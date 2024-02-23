package com.github.silhouettemc.command

import co.aikar.commands.BaseCommand
import co.aikar.commands.annotation.CommandAlias
import co.aikar.commands.annotation.CommandPermission
import co.aikar.commands.annotation.Default
import co.aikar.commands.annotation.Description
import com.github.silhouettemc.util.sendTranslated
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

@CommandAlias("clearchat")
@Description("Clears the chat")
@CommandPermission("silhouettemc.command.clearchat")
object ClearChatCommand : BaseCommand() {

    @Default
    fun onCommand(
        sender: CommandSender,
    ) {

        val clearer = if (sender is Player) sender.name else "Console"
        val clearMessage = "\n".repeat(100) + "<#ffd4e3>The chat has been <#ffb5cf>cleared"

        for (player in Bukkit.getOnlinePlayers()) {
            if (player.hasPermission("silhouettemc.command.clearchat")) {
                if (player == sender) player.sendTranslated("<#ffd4e3>You've cleared the chat for others, but you're immune!")
                else player.sendTranslated("<#ffd4e3>The chat has been cleared by <#ffb5cf>$clearer</#ffb5cf>, but you're immune!")
                continue
            }

            player.sendTranslated(clearMessage)
        }

        Bukkit.getLogger().info("Chat has been cleared by $clearer")

    }

}