package net.recraft.annihilatoin.listener

import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.objects.Game.voidCancel
import net.recraft.annihilatoin.util.Util
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.scheduler.BukkitRunnable

class ListenerFlaggedPlayerVoid: Listener {
    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        val player = event.player
        if (!player.voidCancel()) return
        var to = event.to.clone()
        // xがマイナスでfrom toでマイナスが増加するとき
        if (event.from.x < 0) {
            if (event.from.x > event.to.x) {
                // 検知の範囲を増やしてガラスブロックの中に埋め込まれないようにする
                to.apply{x-=1}
            }
        }
        if (event.from.z < 0) {
            if (event.from.z > event.to.z) {
                // 検知の範囲を増やしてガラスブロックの中に埋め込まれないようにする
                to.apply{z-=1}
            }
        }
        if (Game.mapObject.mapRange.contains(to)) { return }
        val toLoc = event.to.clone()
        val barrier = arrayListOf<Block>()
        (-1..+1).forEach { x1 ->
            (-1..+1).forEach { y1 ->
                (-1..+1).forEach { z1 ->
                    barrier.add(toLoc.clone().apply{x+=x1; y+=y1; z+=z1}.block)
                }
            }
        }
//        event.isCancelled = true
        barrier.forEach { block ->
            if (Game.mapObject.mapRange.contains(block.location)) {return@forEach}
            if (Util.isAir(block)) {
                block.type = Material.GLASS
                object: BukkitRunnable() {
                    override fun run() {
                        block.type = Material.AIR
                    }
                }.runTaskLater(Game.plugin, 20*3)
            }
        }
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