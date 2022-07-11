package net.recraft.annihilatoin.listener

import net.recraft.annihilatoin.database.Database
import net.recraft.annihilatoin.objects.Game
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerLoginEvent
import java.sql.SQLException

class PlayerLoginListener: Listener {
    @EventHandler
    fun playerJoinEvent(event: PlayerLoginEvent) {
        val player = event.player
        val uuid = player.uniqueId
        try {
            val playerData = Database.getPlayerStatus(uuid)
            if (Game.isAlreadyConnected(uuid)) {
                return
            }
            Game.setPlayerData(uuid, playerData)
        } catch (ex: SQLException) {
            event.disallow(PlayerLoginEvent.Result.KICK_FULL, "cannot find your data in database!!!!!")
        }
    }
}