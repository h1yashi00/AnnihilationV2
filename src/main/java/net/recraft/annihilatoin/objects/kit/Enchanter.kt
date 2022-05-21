package net.recraft.annihilatoin.objects.kit

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class Enchanter: KitBase(
    KitType.ENCHANTER,
    Material.EXP_BOTTLE,
    listOf("material")
) {
    override val sword = ItemStack(Material.GOLD_SWORD)
    override fun setInit(player: Player) {
    }
}