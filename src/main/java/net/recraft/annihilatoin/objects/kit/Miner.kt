package net.recraft.annihilatoin.objects.kit

import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class Miner: KitBase(
    KitType.MINER,
    Material.STONE_PICKAXE,
    listOf("aaaaaaa[")
) {
    companion object {
        fun isAffectedOre(material: Material): Boolean
        {
            return when(material) {
                Material.COAL_ORE -> true
                Material.GOLD_ORE -> true
                Material.IRON_ORE -> true
                else -> false;
            }
        }
    }
    override val pickaxe = ItemStack(Material.STONE_PICKAXE) .also {
        it.addUnsafeEnchantment(Enchantment.DIG_SPEED, 1)
    }

    override fun setInit(player: Player) {
    }
}