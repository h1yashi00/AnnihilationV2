package net.recraft.annihilatoin.listener

import net.recraft.annihilatoin.database.Database
import net.recraft.annihilatoin.objects.Game
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import java.sql.SQLException

class PlayerLoginListener: Listener {
    @EventHandler
    fun playerJoinEvent(event: AsyncPlayerPreLoginEvent) {
        val uuid = event.uniqueId
        try {
            Database.getPlayerStatus(uuid)
        } catch (ex: SQLException) {
            event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL, "cannot find your stats data")
        }
    }
}