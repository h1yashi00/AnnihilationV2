package net.recraft.annihilatoin.listener

import net.recraft.annihilatoin.objects.Game
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent


class PlayerAttackEnemyTeam: Listener {
    @EventHandler
    fun onPlayerAttackEnemyTeam(event: EntityDamageByEntityEvent) {
        val attacker : Entity = event.damager
        val victim: Entity = event.entity
        if (attacker !is Player || victim !is Player) return
        val attackerTeam = Game.getTeam(attacker.uniqueId) ?: return
        val victimTeam   = Game.getTeam(victim.uniqueId) ?: return
        if (attackerTeam !== victimTeam) return
        event.isCancelled = true
    }
}