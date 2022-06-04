package net.recraft.annihilatoin.objects.menu

import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.objects.SpecialItem
import net.recraft.annihilatoin.objects.builder.PriceableItemStackBuilder
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class ShopWeaponMenu: Menu("ShopWeapon") {
    companion object {
        fun isSpecialTool(item: ItemStack?): Boolean {
            val lores = item?.itemMeta?.lore ?: return false
            lores.forEach {
                if (it.toRegex().containsMatchIn("Special Tool")) return true
            }
            return false
        }
    }

    override fun createInventory(): Inventory {
        val phaseItems = ArrayList<ItemStack>()
        if (Game.phase.currentPhase >= 4) {
            phaseItems.add(PriceableItemStackBuilder(Material.GOLDEN_APPLE).price(80).damage(1).build())
        }
        val inv = super.createInventory()
        phaseItems.forEach { inv.addItem(it) }
        return inv
    }

    init {
        items.add(PriceableItemStackBuilder(Material.IRON_HELMET)       .price(20).build())
        items.add(PriceableItemStackBuilder(Material.IRON_CHESTPLATE)   .price(20).build())
        items.add(PriceableItemStackBuilder(Material.IRON_LEGGINGS)     .price(20).build())
        items.add(PriceableItemStackBuilder(Material.IRON_BOOTS)        .price(20).build())
        items.add(PriceableItemStackBuilder(Material.IRON_SWORD)        .price(20).build())
        items.add(PriceableItemStackBuilder(Material.BOW)               .price(5).build())
        items.add(PriceableItemStackBuilder(Material.ARROW)             .price(5).amount(32).build())
        items.add(PriceableItemStackBuilder(Material.EXP_BOTTLE)        .price(5).amount(16).build())
        items.add(PriceableItemStackBuilder(Material.COOKED_BEEF)       .price(4).amount(16).build())
        items.add(PriceableItemStackBuilder(Material.DIAMOND_PICKAXE)   .price(20).enchantment(Enchantment.DIG_SPEED, 5).lore(listOf("Special Tool", "special diamond pickaxe that can't mine ores")).build())
        items.add(PriceableItemStackBuilder(Material.DIAMOND_AXE)       .price(20).enchantment(Enchantment.DIG_SPEED, 5).lore(listOf("Special Tool", "special diamond axe that can't mine Logs")).build())
        items.add(PriceableItemStackBuilder(Material.DIAMOND_SPADE)     .price(20).enchantment(Enchantment.DIG_SPEED, 5).lore(listOf("Special Tool", "special diamond spade")).build())
        items.add(PriceableItemStackBuilder(Material.DIAMOND_HOE)       .price(20).enchantment(Enchantment.DIG_SPEED, 5).lore(listOf("Special Tool", "special diamond hoe")).build())
        items.add(PriceableItemStackBuilder(Material.ENCHANTED_BOOK)    .price(20).enchantment(Enchantment.LOOT_BONUS_BLOCKS, 3).build())
        items.add(PriceableItemStackBuilder(Material.ICE)               .price(3).build())
        items.add(PriceableItemStackBuilder(Material.PACKED_ICE)        .price(2).amount(16).build())
        items.add(PriceableItemStackBuilder(Material.BOOK)              .price(1).amount(4).build())
        items.add(PriceableItemStackBuilder(Material.WOOL)              .price(1).amount(16).build())
        items.add(SpecialItem.item)
    }
}