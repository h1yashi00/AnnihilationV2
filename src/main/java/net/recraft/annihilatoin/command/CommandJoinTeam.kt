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
        if (!player.isOp) return false
        // /team [teamColor] じゃなかった場合現在のプレイヤーに現在のチームを知らせる
        val teamColor = if (args?.size == 1) { args[0]!! } else { showPlayerTeamColor(player); return false }
        // playerがTeamに所属していた場合入っていたチームを離脱してからJoinさせる
        val uuid = player.uniqueId
        val team = Game.getTeam(teamColor) ?: return false
        val currentTeam = Game.getTeam(uuid)
        currentTeam?.remove(uuid)
        team.join(uuid)
        if (Game.isStart) {
            player.teleport(team.randomSpawn)
            return true
        }
        player.sendMessage("${ChatColor.GOLD} success!!!")
        return true
    }
    private fun showPlayerTeamColor(player: Player) {
        val msg = Game.getTeam(player.uniqueId)
            ?.let {"you current team is ${Util.getColoredTeamName(it)}"}
            ?: "you have not chosen a team"
        player.sendMessage(msg)
    }
}
