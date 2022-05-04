package net.recraft.annihilatoin.objects.kit

import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack

class Lumberjack: KitBase(Kit.LUMBERJACK, Material.EXP_BOTTLE) {
    override val axe = ItemStack(Material.STONE_AXE).also {
        it.addUnsafeEnchantment(Enchantment.DIG_SPEED, 1)
    }
}