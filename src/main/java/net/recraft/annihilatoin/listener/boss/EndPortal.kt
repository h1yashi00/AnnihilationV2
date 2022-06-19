package net.recraft.annihilatoin.listener.boss

import net.recraft.annihilatoin.objects.Game.team
import net.recraft.annihilatoin.realTeleport
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.WitherSkull
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntitySpawnEvent
import org.bukkit.event.entity.ExplosionPrimeEvent
import org.bukkit.event.player.PlayerPortalEvent
import org.bukkit.event.player.PlayerTeleportEvent

class EndPortal: Listener {
    @EventHandler
    fun onThroughPortal(event: PlayerPortalEvent) {
        val theEnd = Bukkit.getWorld("world_the_end")
        val player = event.player
        if (event.cause != PlayerTeleportEvent.TeleportCause.END_PORTAL) return
        println("${player.world}")
        if (player.world == theEnd) {
            val team = player.team()
            player.realTeleport(team!!.objects.randomSpawn)
            return
        }
        val listOfLoc = listOf(
            Location(theEnd, 12.0, 68.0,-12.0),
            Location(theEnd, 13.0, 68.0, 12.0),
            Location(theEnd, -11.0, 68.0,13.0),
            Location(theEnd, -13.0, 68.0, -12.0)
        )
        player.realTeleport(listOfLoc.random())
    }

}