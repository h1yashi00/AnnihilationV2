package net.recraft.annihilatoin.listener

import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.objects.Game.invisible
import net.recraft.annihilatoin.scoreboard.scoreboard_team.ScoreboardTeamManager
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable

class PlayerInvisible : Listener {
    private fun checkInvisible() {
        Bukkit.getOnlinePlayers().forEach {
            // プレイヤーデータがInvisibleでないのにポーションエフェクトがかかっているとき
            if (!it.invisible()) {
                if (it.activePotionEffects.contains(PotionEffectType.INVISIBILITY)) { ScoreboardTeamManager.setPlayerInv(it) }
            }
            else {
                if (!it.activePotionEffects.contains(PotionEffectType.INVISIBILITY)) { ScoreboardTeamManager.releasePlayerInv(it) }
            }
        }
    }
    init {
        object: BukkitRunnable() {
            override fun run() {
                if (Bukkit.getOnlinePlayers().isNotEmpty()) checkInvisible()
            }
        }.runTaskTimerAsynchronously(Game.plugin, 1, 1)
    }
    fun Collection<PotionEffect>.contains(potionEffectType: PotionEffectType): Boolean {
        this.forEach {
            if (it.type == potionEffectType) return true
        }
        return false
    }

    @EventHandler
    fun onBreak(event: BlockBreakEvent) {
        val player = event.player ?: return
        if (!player.invisible()) return
        player.playSound(player.location, Sound.ZOMBIE_REMEDY, 1F, 1F)
        player.removePotionEffect(PotionEffectType.INVISIBILITY)
    }

    @EventHandler
    fun getDamage(event: EntityDamageEvent) {
        val player = if (event.entity is Player) { event.entity as Player } else { return }
        if (!player.invisible()) return
        player.playSound(player.location, Sound.ZOMBIE_REMEDY, 1F, 1F)
        player.removePotionEffect(PotionEffectType.INVISIBILITY)
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
        ScoreboardTeamManager.joinPacket(player)
    }
}
