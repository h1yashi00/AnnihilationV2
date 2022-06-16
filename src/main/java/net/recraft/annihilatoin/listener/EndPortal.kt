package net.recraft.annihilatoin.listener

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerPortalEvent
import org.bukkit.event.player.PlayerTeleportEvent

class EndPortal: Listener {
    @EventHandler
    fun onThroughPortal(event: PlayerPortalEvent) {
        if (event.cause != PlayerTeleportEvent.TeleportCause.END_PORTAL) return
        val theEnd = Bukkit.getWorld("world_the_end")
        val player = event.player
        player.teleport(Location(theEnd, 100.0, 100.0, 100.0))
    }
}