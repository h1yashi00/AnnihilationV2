package net.recraft.annihilatoin.objects.menu

import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.objects.builder.PriceableItemStackBuilder
import org.bukkit.Material
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class ShopBrewingMenu: Menu("ShopBrewing") {
    override fun createInventory(): Inventory {
        val phaseItems = ArrayList<ItemStack>()
        if (Game.phase.currentPhase >= 3) {
            phaseItems.add(PriceableItemStackBuilder(Material.BLAZE_POWDER).price(20).build())
        }
        val inv = super.createInventory()
        phaseItems.forEach { inv.addItem(it) }
        return inv
    }
    init {
        items.add(PriceableItemStackBuilder(Material.GLASS_BOTTLE)          .price(1).amount(3).build())
        items.add(PriceableItemStackBuilder(Material.BREWING_STAND_ITEM)    .price(15).build())
        items.add(PriceableItemStackBuilder(Material.NETHER_STALK)          .price(5).build())
        items.add(PriceableItemStackBuilder(Material.SUGAR)                 .price(3).build())
        items.add(PriceableItemStackBuilder(Material.GHAST_TEAR)            .price(15).build())
        items.add(PriceableItemStackBuilder(Material.SPIDER_EYE)            .price(3).build())
        items.add(PriceableItemStackBuilder(Material.FERMENTED_SPIDER_EYE)  .price(3).build())
        items.add(PriceableItemStackBuilder(Material.GOLDEN_CARROT)         .price(3).build())
        items.add(PriceableItemStackBuilder(Material.SPECKLED_MELON)        .price(2).build())
        items.add(PriceableItemStackBuilder(Material.RABBIT_FOOT)           .price(5).build())
        items.add(PriceableItemStackBuilder(Material.REDSTONE)              .price(1).build())
        items.add(PriceableItemStackBuilder(Material.GLOWSTONE_DUST)        .price(1).build())
    }
}