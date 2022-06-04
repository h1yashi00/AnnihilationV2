package net.recraft.annihilatoin.objects.kit

import net.recraft.annihilatoin.objects.builder.ItemStackBuilder
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory

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

        fun isItem(item: ItemStack?): Boolean {
            if (item == null) return false
            val meta = item.itemMeta ?: return false
            if (meta.displayName != title) return false
            return true
        }

        val abilityEffectTime: Int = 10
        val coolDown: Int = 20
        private val title = "${ChatColor.GOLD}Gold Rush"
    }
    private fun getItem(): ItemStack {
        return ItemStackBuilder(Material.GOLD_NUGGET)
            .title(title)
            .lore(listOf(
                "右クリックをすることでスキルを発動!",
                "獲得できる鉱石が+1増える"
            ))
            .build()
    }

    override fun setItems(playerInventory: PlayerInventory) {
        super.setItems(playerInventory)
        playerInventory.setItem(3, ItemStack(Material.COAL, 8))
        playerInventory.setItem(4, getItem())
    }
    override val pickaxe = ItemStack(Material.STONE_PICKAXE).also {
        it.addUnsafeEnchantment(Enchantment.DIG_SPEED, 1)
    }
}