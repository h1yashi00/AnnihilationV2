package net.recraft.annihilatoin.listener

import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.objects.PlayerLeaveUnfairAdvantage
import org.bukkit.Bukkit
import org.bukkit.entity.Zombie
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector

class ListenerUnfairZombie(private val unfairAdvantage:PlayerLeaveUnfairAdvantage): Listener {
    @EventHandler
    fun onPlayerAttackZombie(event: EntityDamageByEntityEvent) {
        val zombie = if (event.entity is Zombie) { event.entity as Zombie } else { return }
        val quitPlayer = unfairAdvantage[zombie] ?: return
        val currentHealth = zombie.health
        val finalDamage   = event.finalDamage

        // zombie was killed
        if (currentHealth <= finalDamage) {
            Bukkit.broadcastMessage("zombie was killed")
            quitPlayer.zombieKilled()
            return
        }

        val vec = Vector()
        zombie.velocity = vec
        val runTaskLater = object : BukkitRunnable() {
            override fun run() {
                zombie.velocity = vec
            }
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(Game.plugin, runTaskLater, 1L)
    }
}