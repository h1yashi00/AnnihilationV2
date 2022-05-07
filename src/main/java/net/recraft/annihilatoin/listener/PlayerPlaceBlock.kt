package net.recraft.annihilatoin.listener

import net.recraft.annihilatoin.objects.ResourceBlocks
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent

class PlayerPlaceBlock: Listener {
    @EventHandler
    fun onPlaceBlock(event: PlayerInteractEvent) {
        if (event.player.isOp) return
        val holdingItem = if (event.action != Action.RIGHT_CLICK_BLOCK) { return } else { event.player.itemInHand ?: return }
        if (!ResourceBlocks.isResourceBlocks(holdingItem.type)) return
        event.isCancelled = true
    }
}