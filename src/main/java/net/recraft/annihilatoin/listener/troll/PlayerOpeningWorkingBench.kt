package net.recraft.annihilatoin.listener.troll

import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerInteractEvent
import java.util.*

class PlayerOpeningWorkingBench: Listener {
    private val inventoryOpeningPlayers = HashMap<UUID, Block>()
    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_BLOCK) return
        val block = event.clickedBlock
        if (block.type != Material.WORKBENCH) return
        val player = event.player
        if (player.isSneaking) {
            if (player.itemInHand.type != Material.AIR) {
                // ここに来た場合はプレイヤーにインベントリは開かれない
                return
            }
        }
        inventoryOpeningPlayers[player.uniqueId] = block
    }
    @EventHandler
    fun onPlayerBreakBlock(event: BlockBreakEvent) {
        val player = event.player
        val block = event.block
        if (event.block.type != Material.WORKBENCH) return
        if (!inventoryOpeningPlayers.values.contains(block)) return
        event.isCancelled = true
        player.sendMessage("${ChatColor.RED}他のプレイヤーが使用している作業台は破壊できません")
    }
    @EventHandler
    fun onInventoryClose(event: InventoryCloseEvent) {
        val invType = event.inventory.type
        if (invType != InventoryType.WORKBENCH) return
        val player = event.player
        if (!inventoryOpeningPlayers.contains(player.uniqueId)) return
        inventoryOpeningPlayers.remove(player.uniqueId)
    }
}