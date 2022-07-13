package net.recraft.annihilatoin.admin

import net.recraft.annihilatoin.database.Database
import net.recraft.annihilatoin.database.StaffRank
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CommandGamemode: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (sender !is Player) return false
        val player = sender as Player
        val rank = Database.getRank(player.uniqueId)
        if (rank != StaffRank.OWNER) return false
        if (args[0] == "1") {
            player.sendMessage("${ChatColor.GRAY}Your in Creative")
            player.gameMode = GameMode.CREATIVE
            return true
        }
        else if (args[0] == "0") {
            player.sendMessage("${ChatColor.GRAY}Your in Survival")
            player.gameMode = GameMode.SURVIVAL
            return true
        }
        return false
    }
}