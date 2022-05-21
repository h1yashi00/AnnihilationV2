package net.recraft.annihilatoin.listener.kit

import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.objects.kit.KitType
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Sound
import org.bukkit.entity.FallingBlock
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerToggleFlightEvent
import org.bukkit.scheduler.BukkitRunnable
import java.util.*
import kotlin.collections.HashMap

class ListenerAcrobat: Listener {
    val cooldownPlayers = HashMap<UUID, Int>()
    init {
        object: BukkitRunnable() {
            override fun run() {
                if (cooldownPlayers.isEmpty()) return
                cooldownPlayers.keys.forEach {
                    val time = cooldownPlayers[it]!!
                    cooldownPlayers[it] = time -1
                    if (cooldownPlayers[it]!! < 0) {
                        cooldownPlayers.remove(it)
                        val player = Bukkit.getPlayer(it) ?: return
                        if (Game.getPlayerData(player.uniqueId).kitType == KitType.ACROBAT) {
                            player.allowFlight = true
                            player.world.playSound(player.location, Sound.WITHER_SHOOT, 0.03F, 5F)
                        }
                    }
                }
            }
        }.runTaskTimer(Game.plugin, 0L, 20L)
    }
    @EventHandler
    fun onFallDamage(event: EntityDamageEvent){
        if (event.cause != EntityDamageEvent.DamageCause.FALL) return
        val player = if (event.entity is Player) { event.entity as Player } else return
        val pd = Game.getPlayerData(player.uniqueId)
        if (pd.kitType != KitType.ACROBAT) return
        event.isCancelled = true
    }

    @EventHandler
    fun onPlayerScout(event: PlayerToggleFlightEvent){
        event.isCancelled = true
        val player = event.player
        if (player.gameMode == GameMode.CREATIVE || player.gameMode == GameMode.SPECTATOR || player.isFlying) { return }
        val pd = Game.getPlayerData(player.uniqueId)
        if (pd.kitType != KitType.ACROBAT) return
        // Activate Ability
        player.playSound(player.location, Sound.ZOMBIE_INFECT, 0.5F, 2.0F)
        if (cooldownPlayers.containsKey(player.uniqueId)) return
        cooldownPlayers[player.uniqueId] = 7
        player.allowFlight = false
        player.isFlying = false

        player.velocity = player.location.direction.multiply(1.5).setY(1.2)
    }
}