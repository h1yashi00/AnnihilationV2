package net.recraft.annihilatoin.scoreboard

import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.objects.Game.scoreboard
import net.recraft.annihilatoin.objects.GameTeam
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class ScoreboardTeamTag: ScoreboardBase {
    init {
        val repeatTask = object: BukkitRunnable() {
            override fun run() {
                for (p in Bukkit.getOnlinePlayers()) {
                    p.scoreboard = scoreboard
                }
            }
        }
        repeatTask.runTaskTimerAsynchronously(Game.plugin, 0, 1)
    }

    override fun register() {
        GameTeam.values().forEach {
            registerTeam(it)
        }
    }

    override fun clear() {
    }
    private fun registerTeam(team: GameTeam) {
        scoreboard.registerNewTeam(team.name).apply {
            prefix = team.prefix
            setCanSeeFriendlyInvisibles(true) // in minecraft 1.8.8 invisible portion cant hide player name tag
            setAllowFriendlyFire(false) // the solution is here
        }
    }

    fun registerPlayer(uuid: UUID, newTeam: GameTeam) {
        val offlinePlayer = Bukkit.getOfflinePlayer(uuid)
        val currentTeam = Game.getTeam(uuid) ?: return
        val lastTeamScore = scoreboard.getTeam(currentTeam.name)
        lastTeamScore.removePlayer(offlinePlayer)
        val teamScore = scoreboard.getTeam(newTeam.name)
        teamScore.addPlayer(offlinePlayer)
    }
}