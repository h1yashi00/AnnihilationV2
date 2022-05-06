package net.recraft.annihilatoin.listener

import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.scoreboard.ScoreboardInvisible
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.entity.ThrownPotion
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.ProjectileHitEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.potion.Potion
import org.bukkit.potion.PotionEffectType
import org.bukkit.potion.PotionType
import java.util.*


class PlayerPortionInvisible : Listener {
    private val invisiblePlayers = InvisiblePlayers()

    private class InvisiblePlayers {
        private val uuids: MutableCollection<UUID> = ArrayList()
        fun add(player: Player, duration: Int) {
            ScoreboardInvisible.setInvisible(player)
            uuids.add(player.uniqueId)
            scheduleDelayDuration(player, duration)
        }

        private fun scheduleDelayDuration(player: Player, duration: Int) {
            val scheduler = Bukkit.getScheduler()
            val runnable = Runnable { remove(player) }
            scheduler.scheduleSyncDelayedTask(Game.plugin, runnable, duration.toLong())
        }

        fun remove(player: Player) {
            uuids.remove(player.uniqueId)
            ScoreboardInvisible.releaseInvisible(player)
            val potionEffects = player.activePotionEffects
            for (potionEffect in potionEffects) {
                if (potionEffect.type.name !== PotionEffectType.INVISIBILITY.name) continue
                player.removePotionEffect(potionEffect.type)
            }
        }

        operator fun contains(uuid: UUID): Boolean {
            for (uuid1 in uuids) {
                if (uuid1 === uuid) return true
            }
            return false
        }
    }

    private fun isPlayerEffectedInvisible(player: Player): Int {
        var potionDuration = 0
        val potionEffects = player.activePotionEffects
        for (potionEffect in potionEffects) {
            if (potionEffect.type.name !== PotionEffectType.INVISIBILITY.name) continue
            potionDuration = potionEffect.duration
        }
        return potionDuration
    }

    @EventHandler
    fun hitted(event: ProjectileHitEvent) {
        val entity = event.entity
        if (!(entity.type === EntityType.SPLASH_POTION)) return
        if (entity !is ThrownPotion) return
        val portion = entity as ThrownPotion
        val effects = portion.effects
        var isInvisiblePotion = false
        for (e in effects) {
            if (e.type.name === PotionEffectType.INVISIBILITY.name) isInvisiblePotion = true
        }
        if (!isInvisiblePotion) return
        val scheduler = Bukkit.getScheduler()
        scheduler.scheduleSyncDelayedTask(Game.plugin, Runnable {
            val onlines = Bukkit.getOnlinePlayers()
            onlines.forEach { player ->
                val duration = isPlayerEffectedInvisible(player)
                if (duration == 0) return@forEach
                if (invisiblePlayers.contains(player.uniqueId)) return@forEach
                invisiblePlayers.add(player, duration)
            }
        }, 1)
    }

    @EventHandler
    fun consume(event: PlayerItemConsumeEvent) {
        val consumed = event.item
        if (!consumed.type.equals(Material.POTION)) return
        //It's a potion
        val potion = Potion.fromItemStack(consumed)
        val type = potion.type
        if (type != PotionType.INVISIBILITY) return
        val effects = potion.effects
        var potionDuration = 0
        for (potionEffect in effects) {
            if (potionEffect.type.name !== PotionEffectType.INVISIBILITY.name) continue
            potionDuration = potionEffect.duration
        }
        if (potionDuration == 0) return
        invisiblePlayers.add(event.player, potionDuration)
    }

    @EventHandler
    fun getDamage(event: EntityDamageEvent) {
        val entity: Entity = event.entity as? Player ?: return
        val player = (entity as Player).player
        if (!invisiblePlayers.contains(player.uniqueId)) return
        player.playSound(player.location, Sound.ZOMBIE_REMEDY, 1F, 1F)
        invisiblePlayers.remove(player)
    }
}
