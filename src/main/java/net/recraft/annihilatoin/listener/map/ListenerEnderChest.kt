package net.recraft.annihilatoin.listener.map
import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.objects.map.EnderChest
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.Inventory
import java.util.*


class ListenerEnderChest : Listener {
    @EventHandler
    fun onPlayerOpenEnderChest(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_BLOCK) return
        val rightClickedBlock  = event.clickedBlock
        if (!Game.isEnderChest(rightClickedBlock.location)) return
        event.isCancelled = true
        openEnderChest(event.player)
    }

    @EventHandler
    fun onPlayerBreakEnderChest(event: BlockBreakEvent) {
        if (!Game.isEnderChest(event.block.location)) return
        event.isCancelled = true
    }
    private val inventories: MutableMap<UUID, Inventory> = HashMap()
    private fun openEnderChest(player: Player) {
        val uuid = player.uniqueId
        val inv = if (inventories.containsKey(uuid)) {inventories[uuid]!!}
        else {Bukkit.createInventory(null, 9, player.name + "'s EnderChest")}
        player.openInventory(inv)
        inventories[uuid] = inv
    }
}
