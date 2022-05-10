package net.recraft.annihilatoin.listener

import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Entity
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

//class PlayerPotionInvisible: Listener {
//    private val invisiblePlayers = InvisiblePlayers()
//
//    private class InvisiblePlayers {
//        private val uuids: MutableCollection<UUID> = ArrayList()
//        fun add(player: Player, duration: Int) {
//            ScoreboardNew.setInvisible(player)
//            uuids.add(player.uniqueId)
//            scheduleDelayDuration(player, duration)
//        }
//
//        private fun scheduleDelayDuration(player: Player, duration: Int) {
//            val scheduler = Bukkit.getScheduler()
//            scheduler.scheduleSyncDelayedTask(PlayerPortionInvisible.Companion.plugin,
//                Runnable { remove(player) }, duration)
//        }
//
//        fun remove(player: Player) {
//            uuids.remove(player.uniqueId)
//            ScoreboardNew.releaseInvisible(player)
//            val potionEffects = player.activePotionEffects
//            for (potionEffect in potionEffects) {
//                if (potionEffect.type.name !== PotionEffectType.INVISIBILITY.name) continue
//                player.removePotionEffect(potionEffect.type)
//            }
//        }
//
//        operator fun contains(uuid: UUID): Boolean {
//            for (uuid1 in uuids) {
//                if (uuid1 === uuid) return true
//            }
//            return false
//        }
//    }
//
//    private fun isPlayerEffectedInvisible(player: Player): Int {
//        var potionDuration = 0
//        val potionEffects = player.activePotionEffects
//        for (potionEffect in potionEffects) {
//            if (potionEffect.type.name !== PotionEffectType.INVISIBILITY.name) continue
//            potionDuration = potionEffect.duration
//        }
//        return potionDuration
//    }
//
//    @EventHandler
//    fun hitted(event: ProjectileHitEvent) {
//        val entity = event.entity
//        if (!(entity.getType() === EntityType.SPLASH_POTION)) return
//        if (entity !is ThrownPotion) return
//        val portion = entity as ThrownPotion
//        val effects = portion.effects
//        var isInvisiblePotion = false
//        for (e in effects) {
//            if (e.getType().getName() === PotionEffectType.INVISIBILITY.name) isInvisiblePotion = true
//        }
//        if (!isInvisiblePotion) return
//        val scheduler = Bukkit.getScheduler()
//        scheduler.scheduleSyncDelayedTask(PlayerPortionInvisible.Companion.plugin, Runnable {
//            val onlines = Bukkit.getOnlinePlayers()
//            onlines.forEach { player ->
//                val duration = isPlayerEffectedInvisible(player)
//                if (duration == 0) return@forEach
//                if (invisiblePlayers.contains(player.getUniqueId())) return@forEach
//                invisiblePlayers.add(player, duration)
//            }
//        }, 1)
//    }
//
//    @EventHandler
//    fun consume(event: PlayerItemConsumeEvent) {
//        val consumed = event.item
//        if (!consumed.getType().equals(Material.POTION)) return
//        //It's a potion
//        val potion = Potion.fromItemStack(consumed)
//        val type = potion.type
//        if (type != PotionType.INVISIBILITY) return
//        val effects = potion.effects
//        var potionDuration = 0
//        for (potionEffect in effects) {
//            if (potionEffect.getType().getName() !== PotionEffectType.INVISIBILITY.name) continue
//            potionDuration = potionEffect.getDuration()
//        }
//        if (potionDuration == 0) return
//        invisiblePlayers.add(event.player, potionDuration)
//    }
//
//    @EventHandler
//    fun getDamage(event: EntityDamageEvent) {
//        val entity: Entity = event.entity as? Player ?: return
//        val player = (entity as Player).player
//        if (!invisiblePlayers.contains(player.getUniqueId())) return
//        player.playSound(player.getLocation(), Sound.ZOMBIE_REMEDY, 1, 1)
//        invisiblePlayers.remove(player)
//    }
//
//    companion object {
//        private val plugin: Main = Main.getInstance()
//    }
//}