package net.recraft.annihilatoin.listener

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.events.ListenerPriority
import com.comphenix.protocol.events.PacketAdapter
import com.comphenix.protocol.events.PacketEvent
import net.recraft.annihilatoin.objects.Game
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EnchantingInventory
import org.bukkit.inventory.ItemStack

class PlayerOpenEnchantTable: Listener {
    init {
        Game.protocolManager.addPacketListener(object: PacketAdapter(Game.plugin, ListenerPriority.NORMAL, PacketType.Play.Server.WINDOW_DATA) {
            override fun onPacketSending(event: PacketEvent?) {
                val integers = event!!.packet.integers
                if (integers.read(2).toInt() == -1 || integers.read(1).toInt() < 4)
                    return
                integers.modify(2) {Integer.valueOf(-1)}
            }
        })
    }
    @EventHandler
    fun openEnchantTable(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_BLOCK) return
        if (event.clickedBlock.type != Material.ENCHANTMENT_TABLE) return
        event.isCancelled = true
        val player = event.player
        player.openEnchanting(event.clickedBlock.location, true)
    }
    @EventHandler
    fun onDrop(event: PlayerDropItemEvent) {
        val item = event.itemDrop.itemStack
        if (!item.hasItemMeta()) return
        item.itemMeta.lore?.let {
            if (it.contains("${ChatColor.BOLD}enchant table lapis")) event.itemDrop.remove()
        }
    }
    @EventHandler
    fun InventoryInteract(event: InventoryClickEvent) {
        if (event.inventory !is EnchantingInventory) return
        if (event.currentItem.type != Material.INK_SACK) return
        event.isCancelled = true
    }
    @EventHandler
    fun Intercept(event: InventoryOpenEvent) {
        val enchantInventory = if (event.inventory is EnchantingInventory) { event.inventory } else return
        val lapis = ItemStack(Material.INK_SACK,64, 4)
        val meta = lapis.itemMeta
        meta.lore = ArrayList<String>().apply { add("${ChatColor.BOLD}enchant table lapis")}
        lapis.itemMeta = meta
        enchantInventory.setItem(1, lapis)
    }
}