package net.recraft.annihilatoin.listener

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class ListenerGapple: Listener {
    @EventHandler
    fun onConsume(event: PlayerItemConsumeEvent) {
        val item = event.item
        if (item.type != Material.GOLDEN_APPLE) return
        val player = event.player
        player.addPotionEffect(PotionEffect(PotionEffectType.SLOW_DIGGING, 20 * 5, 3))
        player.addPotionEffect(PotionEffect(PotionEffectType.WEAKNESS, 20 * 10, 3))
    }
}