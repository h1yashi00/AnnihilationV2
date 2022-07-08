package net.recraft.annihilatoin.listener.map

import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.objects.GameTeam
import net.recraft.annihilatoin.objects.ResourceBlocks
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockDispenseEvent
import org.bukkit.event.block.BlockFromToEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent

class GameWorld: Listener {
    @EventHandler(priority = EventPriority.LOWEST)
    fun onPlaceBlockMapRange(event: BlockPlaceEvent) {
        val block = event.block
        if (Game.mapObject.mapRange.contains(block.location)) return
        event.isCancelled = true
    }
    @EventHandler
    fun onPlaceWater(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_BLOCK) return
        if (event.player.itemInHand.type != Material.WATER_BUCKET) return
        val mapRange = Game.mapObject.mapRange
        val loc = event.clickedBlock.location
        GameTeam.values().forEach { if (it.objects.base.contains(loc)){
            event.player.sendMessage("${ChatColor.RED}拠点内への水を設置は禁止されています")
            event.isCancelled = true
            return
        }}
        val x1 = loc.clone().apply{x+=1}
        val x2 = loc.clone().apply{x-=1}
        val z1 = loc.clone().apply{z+=1}
        val z2 = loc.clone().apply{z-=1}
        if ((mapRange.contains(x1) && mapRange.contains(x2) && mapRange.contains(z1) && mapRange.contains(z2))) return
        event.isCancelled = true
        event.player.sendMessage("${ChatColor.RED}奈落側への水の設置は禁止されています")
    }
    @EventHandler
    fun onLiquidFlow(event: BlockFromToEvent) {
        val to = event.toBlock.location
        try {
            if (!Game.mapObject.mapRange.contains(to)) {
                event.isCancelled = true
                event.block.type = Material.GLASS
                to.block.type = Material.AIR
            }
        } catch (ex: UninitializedPropertyAccessException) {
            return
        }
    }
    @EventHandler
    fun onDispenceEvent(event: BlockDispenseEvent) {
        if (event.item.type != Material.WATER_BUCKET) { return }
        event.isCancelled = true
        event.block.type = Material.GLASS
    }
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