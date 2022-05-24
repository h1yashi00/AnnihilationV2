package net.recraft.annihilatoin.listener.kit

import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.objects.kit.Scorpio
import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.scheduler.BukkitRunnable

class ListenerScorpio: Listener {
    @EventHandler
    private fun onInteractEventHoldingNetherStar(event: PlayerInteractEvent) {
        if (!(event.action == Action.RIGHT_CLICK_AIR || event.action == Action.RIGHT_CLICK_BLOCK)) return
        val item = event.item
        if (item.type != Material.NETHER_STAR) return
        if (!Scorpio.isScorpioItem(item)) return
        val player = event.player
        val pd = Game.getPlayerData(player.uniqueId)
//        if (pd.kitType != KitType.SCORPIO) return
        val dropScorpio = player.world.dropItem(player.location.apply {y+=1}, item)
        dropScorpio.pickupDelay = 20 * 5
        val push = player.location.direction
        push.multiply(1)
        dropScorpio.velocity = push
        val repeat = object: BukkitRunnable() {
            override fun run() {
                val hitted = getPlayerByLocation(dropScorpio.location, 1.0F)?: return
                if (hitted.displayName == player.displayName) return
                player.playSound(player.location, Sound.DOOR_CLOSE, 1F,0.2F)
                hitted.playSound(player.location, Sound.DOOR_CLOSE, 1F,0.2F)
                val loc = player.location
                val dir = player.location.direction.apply{multiply(1)}
                loc.add(dir)
                hitted.teleport(loc.apply{y+=2})
                dropScorpio.remove()
                cancel()
            }
        }.runTaskTimer(Game.plugin, 0, 1)
        object: BukkitRunnable() {
            override fun run() {
                repeat.cancel()
                dropScorpio.remove()
            }
        }.runTaskLater(Game.plugin, 20*5)
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