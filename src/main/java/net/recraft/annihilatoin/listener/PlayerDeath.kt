package net.recraft.annihilatoin.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

class PlayerDeath: Listener {
    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        event.entity.spigot().respawn()
    }
}