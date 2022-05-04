package net.recraft.annihilatoin.objects.kit

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory

class Archar: KitBase(Kit.ARCHAR, Material.ARROW){
    override fun setItems(playerInventory: PlayerInventory) {
        super.setItems(playerInventory)
        playerInventory.setItem(3, ItemStack(Material.BOW))
        playerInventory.setItem(4, ItemStack(Material.ARROW, 32))
    }
}