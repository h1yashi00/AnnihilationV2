package net.recraft.annihilatoin.objects.kit

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class Enchanter: KitBase(
    KitType.ENCHANTER,
    ItemStack(Material.EXP_BOTTLE),
    listOf("material")
) {
    override val sword = ItemStack(Material.GOLD_SWORD)
}