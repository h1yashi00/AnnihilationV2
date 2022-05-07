package net.recraft.annihilatoin.scoreboard

import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.objects.Game.scoreboard
import net.recraft.annihilatoin.objects.VoteManager
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Objective

class ScoreboardVote(private val v: VoteManager) : ScoreboardBase {
    private var objective: Objective? = null
    private var canceled = false
    override fun register() {
        val objective = scoreboard.registerNewObjective("vote", "dummy")
        objective.displaySlot = DisplaySlot.SIDEBAR
        objective.displayName = ChatColor.GOLD.toString() + "" + ChatColor.BOLD + "Choose a map!"
        val repeatTask = object: BukkitRunnable() {
            override fun run() {
                if (canceled) { this.cancel(); return }
                for (voteData in v.candidateMaps) {
                    val voteScore = objective.getScore(voteData.worldName)
                    voteScore.score = voteData.amount
                }
            }
        }
        repeatTask.runTaskTimerAsynchronously(Game.plugin, 0, 1)
    }

    override fun clear() {
        val delayTask = object: BukkitRunnable() {
            override fun run() {
                canceled = true
                objective?.unregister()
                objective = null
            }
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(Game.plugin, delayTask, 1)
    }
}