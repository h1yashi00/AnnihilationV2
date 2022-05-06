package net.recraft.annihilatoin.command

import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.objects.kit.KitType
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CommandKit: CommandExecutor {
    override fun onCommand(sender: CommandSender?, command: Command?, label: String?, args: Array<String?>?): Boolean {
        val player = if (sender is Player) { sender.player ?: return false } else { return false }
        val kitName = if (args?.size != 1) return false else { args[0] ?: return false }
        val uuid = player.uniqueId
        Bukkit.broadcastMessage("${ChatColor.RED}$kitName")
        val kitType = KitType.getKitByName(kitName) ?: return false
        Game.setKit(uuid, kitType)
        Bukkit.broadcastMessage("${ChatColor.GOLD}success!! ${ChatColor.AQUA} ${ChatColor.BOLD}your kit is now $kitType")
        return true
    }
}