package net.recraft.annihilatoin.listener.map

import net.recraft.annihilatoin.objects.Game
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.scheduler.BukkitRunnable


class PlayerAttackNexus: Listener {
    private fun delayRespawn(brokenNexus: Block) {
        brokenNexus.type = Material.AIR
        val runnable = object: BukkitRunnable() {
            override fun run() {
                brokenNexus.location.block.type = Material.ENDER_STONE
            }
        }
        Bukkit.getScheduler().scheduleSyncDelayedTask(Game.plugin, runnable, 3)
    }
    @EventHandler
    fun onBreakBlock(event: BlockBreakEvent){
        val brokenBlock = event.block
        if (brokenBlock.type != Material.ENDER_STONE) return
        val damagedNexus = Game.getNexus(brokenBlock) ?: return
        event.isCancelled = true
        val player = event.player
        val playerTeam = Game.getTeam(player.uniqueId) ?: return
        if (playerTeam.nexus == damagedNexus) return
        damagedNexus.damage(player)
        if (damagedNexus.isAlive()) { delayRespawn(brokenBlock); return;}
        damagedNexus.destroyedEvent()
        if (!Game.isFinishStatus()) return
        Game.end()
    }
}