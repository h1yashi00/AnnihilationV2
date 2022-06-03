package net.recraft.annihilatoin.listener

import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.objects.kit.KitGenerator
import net.recraft.annihilatoin.realTeleport
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerRespawnEvent
import org.bukkit.scheduler.BukkitRunnable

class PlayerRespawn: Listener {
    @EventHandler
    fun onRespawn(event: PlayerRespawnEvent) {
        val player = event.player
        if (!Game.isStart) {
            player.realTeleport(Game.lobby.spawnLocation)
            return
        }
        // kit 取得
        val kitType = Game.getPlayerData(player.uniqueId).kitType // default CIVILIAN
        val kit = KitGenerator.get(kitType)
        // プレイヤーにkit装備を渡す
        val team   = Game.getPlayerData(player.uniqueId).team
        team?.let {kit.equip(player.inventory, it.color)}
        val loc = team?.objects?.randomSpawn ?: Game.lobby.spawnLocation

        object: BukkitRunnable() {
            override fun run() {
                player.realTeleport(loc)
            }
        }.runTaskLater(Game.plugin,1)
    }
}