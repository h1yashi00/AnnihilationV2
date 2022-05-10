package net.recraft.annihilatoin.listener

import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.objects.kit.KitGenerator
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.scheduler.BukkitRunnable

class PlayerRespawn: Listener {
    @EventHandler
    fun onRespawn(event: PlayerRespawnEvent) {
        val player = event.player
        // kit 取得
        val kitType = Game.getKit(player.uniqueId) // default CIVILIAN
        val kit = KitGenerator.get(kitType)!!
        // プレイヤーにkit装備を渡す
        val team   = Game.getTeam(player.uniqueId)
        team?.let {kit.equip(player, it.color)}
        val loc = team?.objects?.randomSpawn ?: Game.lobby.spawnLocation
        val delayTask = object: BukkitRunnable() {
            override fun run() {
                player.teleport(loc)
            }
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(Game.plugin, delayTask, 1) // バグ?でoneTick送らせないとrespawnできない
    }
}