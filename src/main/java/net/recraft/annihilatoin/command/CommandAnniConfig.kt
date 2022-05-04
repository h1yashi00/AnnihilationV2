package net.recraft.annihilatoin.command

import net.recraft.annihilatoin.objects.menu.AnniConfigMenu
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CommandAnniConfig: CommandExecutor {
    override fun onCommand(sender: CommandSender?, command: Command?, label: String?, args: Array<out String>?): Boolean {
        val player = if (sender is Player) { sender as Player } else {return false}
        AnniConfigMenu.showMenu(player)
        return true
    }
}
