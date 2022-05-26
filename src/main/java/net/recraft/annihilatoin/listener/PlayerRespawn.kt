package net.recraft.annihilatoin.listener

import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.objects.kit.KitGenerator
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.scheduler.BukkitRunnable

class PlayerRespawn: Listener {
    @EventHandler
    fun onRespawn(event: PlayerRespawnEvent) {
        val player = event.player
        if (!Game.isStart) {
            delaySpawn(player, Game.lobby.spawnLocation)
            return
        }
        // kit 取得
        val kitType = Game.getPlayerData(player.uniqueId).kitType // default CIVILIAN
        val kit = KitGenerator.get(kitType)!!
        // プレイヤーにkit装備を渡す
        val team   = Game.getPlayerData(player.uniqueId).team
        team?.let {kit.equip(player.inventory, it.color)}
        val loc = team?.objects?.randomSpawn ?: Game.lobby.spawnLocation
        delaySpawn(player, loc)
    }
    private fun delaySpawn(player: Player, loc: Location) {
        val delayTask = object: BukkitRunnable() {
            override fun run() {
                player.teleport(loc)
            }
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(Game.plugin, delayTask, 1) // バグ?でoneTick送らせないとrespawnできない
    }
}