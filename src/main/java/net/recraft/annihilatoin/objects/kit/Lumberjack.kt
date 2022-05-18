package net.recraft.annihilatoin.objects.kit

import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

class Lumberjack: KitBase(
    KitType.LUMBERJACK,
    Material.STONE_AXE,
    listOf("maalkjfd[")
) {
    override val axe = ItemStack(Material.STONE_AXE).also {
        it.addUnsafeEnchantment(Enchantment.DIG_SPEED, 1)
    }
}