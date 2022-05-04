package net.recraft.annihilatoin.objects.kit

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class Enchanter: KitBase(Kit.ENCHANTER, Material.EXP_BOTTLE) {
    override val sword = ItemStack(Material.GOLD_SWORD)
}