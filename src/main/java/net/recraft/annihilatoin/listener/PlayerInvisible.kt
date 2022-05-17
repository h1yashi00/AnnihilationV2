package net.recraft.annihilatoin.listener

import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.scoreboard.scoreboard_team.ScoreboardTeamManager
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable

class PlayerInvisible(private val scoreboardTeamManager: ScoreboardTeamManager) : Listener {
    private fun checkInvisible() {
        Bukkit.getOnlinePlayers().forEach {
            val pd = Game.getPlayerData(it.uniqueId)
            // プレイヤーデータがInvisibleでないのにポーションエフェクトがかかっているとき
            if (!pd.invisible) {
                if (it.activePotionEffects.contains(PotionEffectType.INVISIBILITY)) { scoreboardTeamManager.setPlayerInv(it) }
            }
            else {
                if (!it.activePotionEffects.contains(PotionEffectType.INVISIBILITY)) { scoreboardTeamManager.releasePlayerInv(it) }
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
    fun getDamage(event: EntityDamageEvent) {
        val player = if (event.entity is Player) { event.entity as Player } else { return }
        val pd = Game.getPlayerData(player.uniqueId)
        if (!pd.invisible) return
        player.playSound(player.location, Sound.ZOMBIE_REMEDY, 1F, 1F)
        player.removePotionEffect(PotionEffectType.INVISIBILITY)
    }

    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
        scoreboardTeamManager.joinPacket(player)
    }
}
