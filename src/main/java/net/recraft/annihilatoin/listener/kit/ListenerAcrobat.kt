package net.recraft.annihilatoin.listener.kit

import net.recraft.annihilatoin.listener.CoolDown
import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.objects.Game.kitType
import net.recraft.annihilatoin.objects.kit.Acrobat
import net.recraft.annihilatoin.objects.kit.KitType
import org.bukkit.GameMode
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerToggleFlightEvent

class ListenerAcrobat: Listener {
    private val coolDown = CoolDown(
        Acrobat.coolDownTime,
    ) {
        if (it.kitType() == KitType.ACROBAT) {
            it.allowFlight = true
            it.world.playSound(it.location, Sound.WITHER_SHOOT, 0.03F, 5F)
        }
    }
    @EventHandler
    fun onFallDamage(event: EntityDamageEvent){
        if (event.cause != EntityDamageEvent.DamageCause.FALL) return
        val player = if (event.entity is Player) { event.entity as Player } else return
        if (player.kitType() != KitType.ACROBAT) return
        event.isCancelled = true
    }

    @EventHandler
    fun onPlayerScout(event: PlayerToggleFlightEvent){
        val player = event.player
        if (player.gameMode == GameMode.CREATIVE || player.gameMode == GameMode.SPECTATOR || player.isFlying) { return }
        if (player.kitType() != KitType.ACROBAT) return
        // Activate Ability
        event.isCancelled = true
        player.playSound(player.location, Sound.ZOMBIE_INFECT, 0.5F, 2.0F)
        if (!coolDown.isReady(player)) {
            coolDown.isNotReadyMsg(player)
            return
        }
        coolDown.add(player)
        player.allowFlight = false
        player.isFlying = false

        player.velocity = player.location.direction.multiply(1.5).setY(1.2)
    }
}