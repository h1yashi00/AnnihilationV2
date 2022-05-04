package net.recraft.annihilatoin.scoreboard

import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.objects.Game.scoreboard
import net.recraft.annihilatoin.objects.GameTeam
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scoreboard.NameTagVisibility
import java.util.*

class ScoreboardInvisible {
    companion object {
        fun registerTeam(team: GameTeam) {
            val teamScore = scoreboard.registerNewTeam(team.name)
            val invTeamScore = scoreboard.registerNewTeam("inv" + team.name)
            teamScore.prefix = team.prefix
            teamScore.setCanSeeFriendlyInvisibles(true) // in minecraft 1.8.8 invisible portion cant hide players name tag
            teamScore.setAllowFriendlyFire(false) // the solution is player.hideto(player);
            invTeamScore.prefix = team.prefix
            invTeamScore.nameTagVisibility = NameTagVisibility.HIDE_FOR_OTHER_TEAMS
            invTeamScore.setAllowFriendlyFire(false)
        }

        fun setInvisible(player: Player) {
            val team = Game.getTeam(player.uniqueId) ?: return
            val teamScore = scoreboard.getTeam(team.name)
            val offlinePlayer = Bukkit.getOfflinePlayer(player.uniqueId)
            teamScore.removePlayer(offlinePlayer)
            val invTeamScore = scoreboard.getTeam("inv" + team.name)
            invTeamScore.addPlayer(offlinePlayer)
        }

        fun releaseInvisible(player: Player) {
            val team = Game.getTeam(player.uniqueId) ?: return
            val invTeamScore = scoreboard.getTeam("inv" + team.name)
            val offlinePlayer = Bukkit.getOfflinePlayer(player.uniqueId)
            invTeamScore.removePlayer(offlinePlayer)
            val teamScore = scoreboard.getTeam(team.name)
            teamScore.addPlayer(offlinePlayer)
        }

        fun registerPlayer(uuid: UUID, newTeam: GameTeam) {
            val offlinePlayer = Bukkit.getOfflinePlayer(uuid)
            val currentTeam = Game.getTeam(uuid) ?: return
            if (currentTeam != null) {
                val lastTeamScore = scoreboard.getTeam(currentTeam.name)
                lastTeamScore.removePlayer(offlinePlayer)
            }
            val teamScore = scoreboard.getTeam(newTeam.name)
            teamScore.addPlayer(offlinePlayer)
        }

    }

    init {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Game.plugin, {
            for (p in Bukkit.getOnlinePlayers()) {
                p.scoreboard = scoreboard
            }
        }, 0, 10)
    }
}