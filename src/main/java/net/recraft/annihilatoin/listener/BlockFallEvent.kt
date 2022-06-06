package net.recraft.annihilatoin.listener

import org.bukkit.Material
import org.bukkit.entity.FallingBlock
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityChangeBlockEvent

class BlockFallEvent: Listener {
    @EventHandler
    fun onFallBlock(event: EntityChangeBlockEvent) {
        val fallingEntity = if (event.entity is FallingBlock) event.entity as FallingBlock else return
        if (fallingEntity.material != Material.GRAVEL) return
        event.isCancelled = true
        val block = fallingEntity.location.clone().apply{y-=1}.block
        block.type = Material.BARRIER
    }
}