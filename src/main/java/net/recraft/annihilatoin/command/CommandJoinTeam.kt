package net.recraft.annihilatoin.command

import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.util.Util
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player


class CommandJoinTeam: CommandExecutor {
    override fun onCommand(sender: CommandSender?, command: Command?, label: String?, args: Array<String?>?): Boolean {
        val player = if (sender is Player) { sender.player ?: return false } else { return false }
        val teamColor = if (args?.size == 1) { args[0] } else {null}
        if (teamColor == null) { showPlayerTeamColor(player); return false}
        val team = Game.getTeam(teamColor) ?: return false
        // playerがTeamに所属していた場合入っていたチームを離脱してからJoinさせる
        val playerTeam = Game.getTeam(player.uniqueId)
        playerTeam ?: playerTeam?.remove(player.uniqueId)
        team.join(player.uniqueId)
        player.sendMessage("${ChatColor.GOLD} success!!!")
        return true
    }
    private fun showPlayerTeamColor(player: Player) {
        val team = Game.getTeam(player.uniqueId)
        if (team == null) {
            player.sendMessage("you have not chosen a team")
            return
        }
        player.sendMessage("Your current team is " + Util.getColoredTeamName(team))
    }
}
