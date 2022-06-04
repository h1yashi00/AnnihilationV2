package net.recraft.annihilatoin.listener

import net.recraft.annihilatoin.objects.Game

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.scheduler.BukkitRunnable

class PlayerDeath: Listener {
    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        event.deathMessage = ""
        object:BukkitRunnable() {
            override fun run() {
                event.entity.spigot().respawn()
            }
        }.runTaskLater(Game.plugin, 1)
    }
}