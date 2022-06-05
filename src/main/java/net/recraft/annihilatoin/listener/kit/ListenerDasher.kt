package net.recraft.annihilatoin.listener.kit

import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.objects.Game.kitType
import net.recraft.annihilatoin.objects.kit.Dasher
import net.recraft.annihilatoin.objects.kit.KitType
import net.recraft.annihilatoin.util.ParticleEffect
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector

class ListenerDasher: Listener {
    @EventHandler
    fun onConsume(event: PlayerItemConsumeEvent) {
        val player = event.player
        if (player.kitType() != KitType.DASHER) return
        val foodLevel = player.foodLevel
        player.setFoodLevel(foodLevel +3)
    }
    @EventHandler
    fun onClickDasherItem(event: PlayerInteractEvent) {
        if (!(event.action == Action.RIGHT_CLICK_AIR || event.action == Action.RIGHT_CLICK_BLOCK)) return
        if (event.item == null) return
        if (!Dasher.isKitItem(event.item)) return
        event.isCancelled = true
        val player = event.player
        if (player.foodLevel < 0) {
            if (player.gameMode == GameMode.CREATIVE) {
                player.setFoodLevel(20)
            }
            player.sendMessage("${ChatColor.RED}eat food!!!!")
            return
        }
        activeAbility(player)
    }

    private fun oneBlockTeleport(player: Player, firstDir: Vector, food: Boolean): Boolean {
        val foodLevel = player.foodLevel
        if (foodLevel < 0) return false
        val location = player.location
        val pitchNew    = player.location.pitch
        val yawNew      = player.location.yaw
        val added = location.add(firstDir)
        if(added.block.type != Material.AIR) return false
        if(added.clone().add(firstDir).block.type != Material.AIR) return false
        if(added.clone().add(firstDir).add(firstDir).block.type != Material.AIR) return false
        if(added.clone().apply{y+=1}.block.type != Material.AIR) return false
        if(added.clone().apply{y+=2}.block.type != Material.AIR) return false
        ParticleEffect.FIREWORKS_SPARK.display(0F, 0F, 0F, 0F, 1, added, 256.0)
        if (food) {
            player.setFoodLevel(foodLevel -1)
        }
        player.teleport(added.apply{pitch = pitchNew; yaw = yawNew})
        return true
    }

    private fun activeAbility(player: Player) {
        val firstDir = player.location.direction.clone()
        object: BukkitRunnable() {
            var count = 0
            override fun run() {
                val food = count % 2 == 0
                if (!oneBlockTeleport(player,firstDir, food)) {
                    cancel()
                }
                if (5 < count) {
                    player.world.playSound(player.location, Sound.ENDERMAN_TELEPORT,1f,1f)
                    cancel()
                }
                count += 1
            }
        }.runTaskTimerAsynchronously(Game.plugin, 0,1)
    }
}