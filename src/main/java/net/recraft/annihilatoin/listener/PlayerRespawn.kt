package net.recraft.annihilatoin.listener

import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.objects.Game.kitType
import net.recraft.annihilatoin.objects.Game.team
import net.recraft.annihilatoin.objects.kit.KitGenerator
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerRespawnEvent

class PlayerRespawn: Listener {
    @EventHandler
    fun onRespawn(event: PlayerRespawnEvent) {
        val player = event.player
        if (!Game.isStart) {
            event.respawnLocation = Game.lobby.spawnLocation
            return
        }
        // kit 取得
        val kitType = player.kitType() // default CIVILIAN
        val kit = KitGenerator.get(kitType)
        // プレイヤーにkit装備を渡す
        val team   = player.team()
        team?.let {kit.equip(player.inventory, it.color)}
        val loc = team?.objects?.randomSpawn ?: Game.lobby.spawnLocation
        event.respawnLocation = loc
    }
}