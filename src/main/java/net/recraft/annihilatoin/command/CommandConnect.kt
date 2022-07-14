package net.recraft.annihilatoin.command

import net.recraft.annihilatoin.objects.Game
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import java.io.ByteArrayOutputStream
import java.io.DataOutputStream
import java.io.IOException

class CommandConnect: CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        val player = if (sender is Player) sender as Player else return false
        sendBungee(player, args[0])
        return true
    }

    private fun sendBungee(player: Player, server: String) {
        val byteArrayOutputStream = ByteArrayOutputStream()
        val dataOutputStream      = DataOutputStream(byteArrayOutputStream)
        try {
            dataOutputStream.writeUTF("Connect")
            dataOutputStream.writeUTF(server)
        } catch(ex: IOException) {
            ex.printStackTrace()
        }
        player.sendPluginMessage(Game.plugin, "BungeeCord", byteArrayOutputStream.toByteArray())
        player.sendMessage("${ChatColor.GRAY}Connecting $server ...")
    }
}