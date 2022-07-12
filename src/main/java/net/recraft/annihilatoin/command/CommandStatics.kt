package net.recraft.annihilatoin.command

import net.recraft.annihilatoin.database.Database
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CommandStatics: CommandExecutor {
    override fun onCommand(sender: CommandSender?, p1: Command?, p2: String?, p3: Array<out String>?): Boolean {
        val player = if (sender is Player) { sender as Player } else return false
        player.sendMessage("${Database.getPlayerStatus(player.uniqueId)}}")
        return true
    }
}