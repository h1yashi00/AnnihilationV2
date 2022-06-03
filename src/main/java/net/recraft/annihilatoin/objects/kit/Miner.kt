package net.recraft.annihilatoin.objects.kit

import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class Miner: KitBase(
    KitType.MINER,
    ItemStack(Material.STONE_PICKAXE),
    listOf("取れる鉱石の量が二倍になる")
) {
    companion object {
        fun isAffectedOre(material: Material): Boolean
        {
            return when(material) {
                Material.GOLD_ORE -> true
                Material.IRON_ORE -> true
                else -> false;
            }
        }
    }
    override val pickaxe = ItemStack(Material.STONE_PICKAXE) .also {
        it.addUnsafeEnchantment(Enchantment.DIG_SPEED, 1)
    }
}