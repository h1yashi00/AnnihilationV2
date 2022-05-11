package net.recraft.annihilatoin.objects.menu

import net.recraft.annihilatoin.objects.builder.ItemStackBuilder
import net.recraft.annihilatoin.objects.builder.PriceableItemStackBuilder
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment

class ShopWeaponMenu: Menu("ShopWeapon") {
    init {
        items.add(PriceableItemStackBuilder(Material.IRON_HELMET)       .price(20).build())
        items.add(PriceableItemStackBuilder(Material.IRON_CHESTPLATE)   .price(20).build())
        items.add(PriceableItemStackBuilder(Material.IRON_LEGGINGS)     .price(20).build())
        items.add(PriceableItemStackBuilder(Material.IRON_BOOTS)        .price(20).build())
        items.add(PriceableItemStackBuilder(Material.IRON_SWORD)        .price(20).build())
        items.add(PriceableItemStackBuilder(Material.BOW)               .price(5).build())
        items.add(PriceableItemStackBuilder(Material.ARROW)             .price(5).amount(32).build())
        items.add(PriceableItemStackBuilder(Material.EXP_BOTTLE)        .price(5).amount(16).build())
        items.add(PriceableItemStackBuilder(Material.COOKED_BEEF)       .price(20).amount(16).build())
        items.add(PriceableItemStackBuilder(Material.DIAMOND_PICKAXE)   .price(20).enchantment(Enchantment.DIG_SPEED, 5).lore("special diamond axe that can't mine ores").build())
        items.add(PriceableItemStackBuilder(Material.DIAMOND_AXE)       .price(20).enchantment(Enchantment.DIG_SPEED, 5).lore("special diamond axe that can't mine ores").build())
        items.add(PriceableItemStackBuilder(Material.DIAMOND_SPADE)     .price(20).enchantment(Enchantment.DIG_SPEED, 5).lore("special diamond axe that can't mine ores").build())
        items.add(PriceableItemStackBuilder(Material.DIAMOND_HOE)       .price(20).enchantment(Enchantment.DIG_SPEED, 5).lore("special diamond axe that can't mine ores").build())
    }
}