package net.recraft.annihilatoin.scoreboard

import net.recraft.annihilatoin.objects.Game
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scoreboard.DisplaySlot
import java.util.*

class ScoreboardNexusStatus {
    fun registerNexusScoreboard() {
        val objective = Game.scoreboard.registerNewObjective("world", "dummy")
        objective.displaySlot = DisplaySlot.SIDEBAR
        objective.displayName =
            ChatColor.GOLD.toString() + "" + ChatColor.BOLD + "Map " + Game.map.name
        val runnable = object : BukkitRunnable() {
            override fun run() {
                Game.teams.forEach {
                    val teamHealth = objective.getScore(
                        "${it.color}${it.name.toLowerCase(Locale.ROOT)}Nexus"
                    )
                    teamHealth.score = it.nexus.hp
                }
            }
        }
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Game.plugin, runnable, 0, 1)
    }
}