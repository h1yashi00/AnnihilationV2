package net.recraft.annihilatoin.objects.kit

import net.recraft.annihilatoin.objects.builder.ItemStackBuilder
import net.recraft.annihilatoin.objects.builder.KitClassIconCreator
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory

class Civilian: KitBase(
    KitType.CIVILIAN,
    Material.PORTAL,
    listOf("you can use spade as a default! so cool",
                "material",
                "material",
                "material")
) {
    override fun setItems(playerInventory: PlayerInventory) {
        super.setItems(playerInventory)
        playerInventory.setItem(3, ItemStack(Material.WOOD_SPADE))
    }
}