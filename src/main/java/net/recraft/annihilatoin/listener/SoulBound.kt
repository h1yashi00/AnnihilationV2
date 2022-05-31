package net.recraft.annihilatoin.listener

import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.inventory.*
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class SoulBound : Listener {
    private val playerOpeningInventory = HashMap<UUID, Inventory>()
    @EventHandler
    fun onOpenInventory(event: InventoryOpenEvent) {
        val player = event.player
        playerOpeningInventory[player.uniqueId] = event.inventory
    }
    @EventHandler
    fun onCloseInventory(event: InventoryCloseEvent) {
        val player = event.player
        playerOpeningInventory.remove(player.uniqueId)
    }

    @EventHandler
    fun onPlayerInventoryClick1(event: InventoryClickEvent) {
        if (event.inventory.title == NetherGate.kitItemTitle) return
        if (event.click != ClickType.NUMBER_KEY) return
        val player = event.whoClicked
        if (!playerOpeningInventory.contains(player.uniqueId)) return
        val inv = event.clickedInventory
        if (inv.type != InventoryType.CHEST) return

        val pushedButton = event.hotbarButton
        val chooseItem = player.inventory.getItem(pushedButton) ?: return
        if (!isSoulbound(chooseItem)) return
        event.isCancelled = true
    }

    @EventHandler
    fun onPlayerInventoryClick2(event: InventoryClickEvent) {
        if (event.inventory.title == NetherGate.kitItemTitle) return
        if (!isSoulbound(event.currentItem)) return
        val player = event.whoClicked
        if (!playerOpeningInventory.contains(player.uniqueId)) return
        val openedInv = playerOpeningInventory[player.uniqueId]!!
        if (openedInv.type != InventoryType.CHEST) return
        event.isCancelled = true
    }

    @EventHandler
    fun onEntityDeath(event: EntityDeathEvent) {
        val it = event.drops.iterator()
        while (it.hasNext()) {
            if (isSoulbound(it.next())) {
                it.remove()
            }
        }
    }
    @EventHandler
    fun onPlayerDropItem(event: PlayerDropItemEvent) {
        val item = event.itemDrop.itemStack
        if (!isSoulbound(item)) return
        val player = event.player
        player.playSound(player.location, Sound.BLAZE_HIT, 0.5f, 0.1f)
        event.itemDrop.remove()
    }

    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        val it = event.drops.iterator()
        while (it.hasNext()) {
            if (isSoulbound(it.next())) {
                it.remove()
            }
        }
    }

    companion object {
        fun isSoulbound(item: ItemStack?): Boolean {
            if (item == null) return false
            val meta = item.itemMeta ?: return false
            val lores = meta.lore ?: return false
            for (lore in lores) {
                if (lore.equals(data)) {
                    return true
                }
            }
            return false
        }

        private val data: String = ChatColor.YELLOW.toString() + "Soulbound"
        fun set(item: ItemStack) {
            val meta = item.itemMeta
            val lores = meta.lore
            if (lores == null) {
                val lore: MutableList<String> = ArrayList()
                lore.add(data)
                meta.lore = lore
            } else {
                lores.add(data)
                meta.lore = lores
            }
            item.itemMeta = meta
        }
    }
}
