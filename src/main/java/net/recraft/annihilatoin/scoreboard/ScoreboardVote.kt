package net.recraft.annihilatoin.scoreboard

import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.objects.Game.scoreboard
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.scoreboard.DisplaySlot

class ScoreboardVote {
    fun registerVote() {
        val objective = scoreboard.registerNewObjective("vote", "dummy")
        objective.displaySlot = DisplaySlot.SIDEBAR
        objective.displayName = ChatColor.GOLD.toString() + "" + ChatColor.BOLD + "Choose a map!"
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Game.plugin, {
            for (voteData in Game.candidateMaps) {
                val voteScore = objective.getScore(voteData.worldName)
                voteScore.score = voteData.amount
            }
        }, 0, 1)
    }
}