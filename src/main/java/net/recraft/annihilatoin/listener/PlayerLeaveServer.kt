package net.recraft.annihilatoin.listener

import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.objects.Game.team
import net.recraft.annihilatoin.objects.PlayerLeaveUnfairAdvantage
import net.recraft.annihilatoin.util.Util
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.scheduler.BukkitRunnable

class PlayerLeaveServer(
    private val playerLeaveUnfairAdvantage: PlayerLeaveUnfairAdvantage,
): Listener {
    @EventHandler
    fun onPlayerLeave(event: PlayerQuitEvent) {
        val player = event.player
        if (!Game.isStart) return
        // ゲームが始まっていてチームに所属している
        val team = player.team() ?: return
        val leaveLocation = player.location
        val hp =  player.health
        val inventory = player.inventory
        playerLeaveUnfairAdvantage.add(player.uniqueId, leaveLocation, Util.getColoredPlayersName(player, team), hp, inventory)
        val runnable = object: BukkitRunnable() {
            override fun run() {
                playerLeaveUnfairAdvantage[player.uniqueId]?.survived()
            }
        }
        val oneMinute = 20 * 60
        Bukkit.getScheduler().scheduleSyncDelayedTask(Game.plugin, runnable, oneMinute.toLong())

    }
}