package net.recraft.annihilatoin.scoreboard

import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.objects.GameTeam
import net.recraft.annihilatoin.util.Util
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Objective

class ScoreboardNexusStatus {
    private var objective: Objective? = null
    private var repeatTask: BukkitRunnable? = null
    fun register() {
        objective = Game.scoreboard.registerNewObjective("world", "dummy")
        objective?.displaySlot = DisplaySlot.SIDEBAR
        objective?.displayName =
            ChatColor.GOLD.toString() + "" + ChatColor.BOLD + "Map " + Game.map.name
        repeatTask = object : BukkitRunnable() {
            override fun run() {
                GameTeam.values().forEach {
                    val teamHealth = objective?.getScore(
                        "${Util.getColoredTeamName(it)}${it.chatColor} Nexus"
                    )
                    teamHealth?.score = it.objects.nexus.hp
                }
            }
        }
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Game.plugin, repeatTask, 0, 1)
    }
    fun clear() {
        objective?.unregister()
        objective = null
        repeatTask?.cancel()
        repeatTask = null
    }
}