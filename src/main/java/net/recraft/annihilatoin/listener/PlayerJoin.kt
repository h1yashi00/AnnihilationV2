package net.recraft.annihilatoin.listener

import net.recraft.annihilatoin.database.Database
import net.recraft.annihilatoin.objects.Game
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerJoin: Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    fun playerJoinEvent(event: PlayerJoinEvent) {
        val player = event.player
        val uuid = player.uniqueId
        if (Game.isAlreadyConnected(uuid)) {
            return
        }
        val playerData = Database.getPlayerStatus(uuid)
        Game.setPlayerData(uuid, playerData)
    }
}