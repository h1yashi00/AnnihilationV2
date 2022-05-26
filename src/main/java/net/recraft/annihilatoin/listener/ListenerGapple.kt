package net.recraft.annihilatoin.listener

import net.recraft.annihilatoin.objects.Game
import org.bukkit.Effect
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable

class ListenerGapple: Listener {
    @EventHandler
    fun onConsume(event: PlayerItemConsumeEvent) {
        val item = event.item
        if (item.type != Material.GOLDEN_APPLE) return
        val player = event.player
        player.addPotionEffect(PotionEffect(PotionEffectType.SLOW_DIGGING, 20 * 5, 3))
        player.addPotionEffect(PotionEffect(PotionEffectType.WEAKNESS, 20 * 10, 3))
        val effect = object: BukkitRunnable() {
            override fun run() {
                player.world.playEffect(player.location.apply{y+=2.5}, Effect.HEART,1)
            }
        }.runTaskTimer(Game.plugin, 0,5)

        object: BukkitRunnable() {
            override fun run() {
                effect.cancel()
            }
        }.runTaskLater(Game.plugin, 20*30)
    }
}