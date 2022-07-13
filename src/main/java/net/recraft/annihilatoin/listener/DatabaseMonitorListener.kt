package net.recraft.annihilatoin.listener

import net.recraft.annihilatoin.database.AnnihilationStatsColumn
import net.recraft.annihilatoin.database.Database
import net.recraft.annihilatoin.objects.Game
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.scheduler.BukkitRunnable

class DatabaseMonitorListener: Listener {
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun blockBreakEvent(event: BlockPlaceEvent) {
        object: BukkitRunnable() {
            override fun run() {
                Database.incCount(AnnihilationStatsColumn.BLOCKS_BROKEN, event.player.uniqueId)
            }
        }.runTaskAsynchronously(Game.plugin)
    }
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    fun blockPlaceEvent(event: BlockPlaceEvent) {
        object: BukkitRunnable() {
            override fun run() {
                Database.incCount(AnnihilationStatsColumn.BLOCKS_PLACED, event.player.uniqueId)
            }
        }.runTaskAsynchronously(Game.plugin)
    }
}