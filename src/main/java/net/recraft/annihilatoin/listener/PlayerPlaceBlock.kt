package net.recraft.annihilatoin.listener

import net.recraft.annihilatoin.objects.ResourceBlocks
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent

class PlayerPlaceBlock: Listener {
    @EventHandler
    fun onPlaceBlock(event: BlockPlaceEvent) {
        if (event.player.isOp) return
        val holdingItem = event.block
        if (!ResourceBlocks.isResourceBlocks(holdingItem.type)) return
        event.isCancelled = true
    }
    @EventHandler
    fun onPlaceBlock2(event: BlockPlaceEvent) {
        if (128 < event.block.location.y) {
            event.player.sendMessage("${ChatColor.RED}高すぎます!!128<=y以上はブロックを置くことができません")
            event.isCancelled = true
        }
    }
}