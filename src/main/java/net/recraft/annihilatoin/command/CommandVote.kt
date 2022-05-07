package net.recraft.annihilatoin.command

import net.recraft.annihilatoin.objects.VoteManager
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class CommandVote(private val v: VoteManager) : CommandExecutor {
    override fun onCommand(sender: CommandSender?, command: Command?, label: String?, args : Array<out String>?): Boolean {
        val player = (sender as? Player) ?: return false
        val mapName = args.let {
            it ?: return false
            if (it.size !=  1) return false
            it[0]
        }
        if (player.uniqueId in v) { v.revote(player.uniqueId) }
        v.vote(player.uniqueId, mapName)
        return true
    }
}