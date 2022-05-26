package net.recraft.annihilatoin.listener

import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.util.Util
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.scheduler.BukkitRunnable

class ListenerFlaggedPlayerVoid: Listener {
    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        val player = event.player
        val pd = Game.getPlayerData(player.uniqueId)
        if (!pd.voidCancel) return
        if (!Util.isUnderVoid(event.to)) return
        val changeLocation = a(player.location.apply { y -= 1 })
        changeLocation.removeIf { (!Util.isUnderVoid(it)) }
        changeLocation.forEach { it.block.type = Material.GLASS }
        object : BukkitRunnable() {
            override fun run() {
                changeLocation.forEach { it.block.type = Material.AIR }
            }
        }.runTaskLater(Game.plugin, 20)
    }

    private fun a(loc: Location): MutableSet<Location> {
        return mutableSetOf<Location>().apply {
            for (i in 0..4) {
                for (j in 0..4) {
                    add(loc.clone().apply { x += i; z += j })
                }
            }
        }
    }
}