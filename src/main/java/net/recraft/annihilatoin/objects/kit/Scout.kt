package net.recraft.annihilatoin.objects.kit

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory

class Scout: KitBase(
    KitType.SCOUT,
    ItemStack(Material.FISHING_ROD),
    listOf("グラップルを使って自由自在にマップを駆け回ろう!")
){
    companion object {
        val combatTagCoolDown = 2
        private val scoutFishLore= "${ChatColor.GOLD}Grapple"
        fun isScoutFishingRod(itemStack: ItemStack?): Boolean {
            if (itemStack == null) return false
            if (itemStack.itemMeta == null) return false
            if (itemStack.itemMeta.lore == null) return false
            itemStack.itemMeta.lore.forEach {
                if (it == scoutFishLore) return true
            }
            return false
        }
    }
    private val scoutFishingRod = ItemStack(Material.FISHING_ROD).apply{
        val lore = ArrayList<String>().apply {
            add(scoutFishLore)
        }
        val meta = Bukkit.getItemFactory().getItemMeta(Material.FISHING_ROD)
        meta.lore = lore
        itemMeta = meta
    }

    override fun setItems(playerInventory: PlayerInventory) {
        super.setItems(playerInventory)
        playerInventory.setItem(3, scoutFishingRod)
    }
}