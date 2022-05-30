package net.recraft.annihilatoin.objects.kit

import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class Lumberjack: KitBase(
    KitType.LUMBERJACK,
    ItemStack(Material.STONE_AXE),
    listOf("取れる原木が2倍に!?")
) {
    override val axe = ItemStack(Material.STONE_AXE).also {
        it.addUnsafeEnchantment(Enchantment.DIG_SPEED, 1)
    }
}