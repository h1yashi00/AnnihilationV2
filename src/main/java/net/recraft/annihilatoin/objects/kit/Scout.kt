package net.recraft.annihilatoin.objects.kit

import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory

class Scout: KitBase(Kit.SCOUT, Material.FISHING_ROD){
    companion object {
        fun isScoutFishingRod(itemStack: ItemStack): Boolean {
            if (scoutFishingRod == itemStack) return true
            return false
        }
        private val scoutFishingRod = ItemStack(Material.FISHING_ROD).apply{
            itemMeta.lore.add("${ChatColor.GOLD}${type.name}")
        }
    }
    override fun setItems(playerInventory: PlayerInventory) {
        super.setItems(playerInventory)
        playerInventory.setItem(3, scoutFishingRod)
    }
}