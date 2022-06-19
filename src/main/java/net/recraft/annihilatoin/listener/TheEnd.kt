package net.recraft.annihilatoin.listener

import org.bukkit.Bukkit
import org.bukkit.entity.EntityType
import org.bukkit.entity.WitherSkull
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntitySpawnEvent
import org.bukkit.event.entity.ExplosionPrimeEvent

class TheEnd: Listener {
    private val world = Bukkit.getWorld("world_the_end")
    @EventHandler
    fun onBreak(event: BlockBreakEvent) {
        if (event.block.world == world) {
            event.isCancelled = true
        }
    }
    @EventHandler
    fun onPlace(event: BlockPlaceEvent) {
        if (event.block.world == world) {
            event.isCancelled = true
        }
    }
    @EventHandler
    fun theEndSpawn(event: EntitySpawnEvent) {
        if (event.entity.world != Bukkit.getWorld("world_the_end")) return
        if (event.entity.type != EntityType.ENDERMAN) return
        event.isCancelled = true
    }

    @EventHandler
    fun breakBlock(event: ExplosionPrimeEvent) {
        if (event.entity !is WitherSkull) return
        event.isCancelled = true
    }
}