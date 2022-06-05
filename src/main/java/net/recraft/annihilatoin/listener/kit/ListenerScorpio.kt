package net.recraft.annihilatoin.listener.kit

import net.recraft.annihilatoin.listener.CoolDown
import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.objects.Game.kitType
import net.recraft.annihilatoin.objects.Game.setVoidCancel
import net.recraft.annihilatoin.objects.Game.team
import net.recraft.annihilatoin.objects.kit.KitType
import net.recraft.annihilatoin.objects.kit.Scorpio
import net.recraft.annihilatoin.util.ParticleEffect
import net.recraft.annihilatoin.util.Util
import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.scheduler.BukkitRunnable

class ListenerScorpio: Listener {
    private val coolDown = CoolDown(
        Scorpio.coolDownTime
    ){}
    @EventHandler
    private fun onInteractEventHoldingNetherStar(event: PlayerInteractEvent) {
        if (!(event.action == Action.RIGHT_CLICK_AIR || event.action == Action.RIGHT_CLICK_BLOCK)) return
        val item = event.item ?: return
        if (item.type != Material.NETHER_STAR) return
        if (!Scorpio.isScorpioItem(item)) return
        val player = event.player
        if (player.kitType() != KitType.SCORPIO) return
        if (!coolDown.isReady(player)) {
            coolDown.isNotReadyMsg(player)
            return
        }
        coolDown.add(player)
        // Activate Ability!!
        val dropScorpio = player.world.dropItem(player.location.apply {y+=1}, item)
        dropScorpio.pickupDelay = 20 * 5
        val push = player.location.direction
        push.multiply(1)
        dropScorpio.velocity = push
        val repeat = object: BukkitRunnable() {
            override fun run() {
                ParticleEffect.FIREWORKS_SPARK.display(0F, 0F, 0F, 0F, 1, dropScorpio.location, 256.0)
                val hitted = getPlayerByLocation(dropScorpio.location, 1.0F)?: return
                if (hitted.displayName == player.displayName) return
                val hittedTeam = hitted.team()
                if (player.team() == hittedTeam) return
                if (Util.isUnderVoid(player.location)) {
                    player.sendMessage("${ChatColor.RED}奈良スコピは許可されていません")
                    return
                }
                // フックが当たり､チェックが完了
                object: BukkitRunnable() {
                    override fun run() {
                        // 5秒間Voidに行けない
                        hitted.setVoidCancel(false)
                    }
                }.runTaskLater(Game.plugin, 20*5)
                hitted.setVoidCancel(true)
                player.playSound(player.location, Sound.DOOR_CLOSE, 1F,0.2F)
                hitted.playSound(player.location, Sound.DOOR_CLOSE, 1F,0.2F)
                playEffect(hitted.location.apply {y+=1})
                val loc = player.location
                val dir = player.location.direction.apply{multiply(1)}
                loc.add(dir)
                hitted.teleport(loc.apply{y+=2})
                dropScorpio.remove()
                cancel()
            }
        }.runTaskTimer(Game.plugin, 0, 2)
        object: BukkitRunnable() {
            override fun run() {
                repeat.cancel()
                dropScorpio.remove()
            }
        }.runTaskLater(Game.plugin, 20*5)
    }
    private fun playEffect(location: Location) {
        ParticleEffect.SNOW_SHOVEL.display(0F, 0F, 0F, 0F, 1, location, 256.0)
    }
    private fun getPlayerByLocation(loc: Location, r: Float): Player? {
        loc.world.entities.forEach {
            if (it.location.distanceSquared(loc) < r)
                if (it is Player) return it.player
            if (it.location.apply{y+=1}.distanceSquared(loc) < r)
                if (it is Player) return it.player
        }
        return null
    }
}