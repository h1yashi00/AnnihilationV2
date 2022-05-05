package net.recraft.annihilatoin.objects.menu

import net.recraft.annihilatoin.objects.builder.PriceableItemStackBuilder
import org.bukkit.Material

class ShopBrewingMenu: Menu("ShopBrewing") {
    init {
        items.add(PriceableItemStackBuilder(Material.GLASS_BOTTLE)          .price(1).build())
        items.add(PriceableItemStackBuilder(Material.BREWING_STAND)         .price(20).build())
        items.add(PriceableItemStackBuilder(Material.SUGAR)                 .price(3).build())
        items.add(PriceableItemStackBuilder(Material.GHAST_TEAR)            .price(15).build())
        items.add(PriceableItemStackBuilder(Material.SPIDER_EYE)            .price(3).build())
        items.add(PriceableItemStackBuilder(Material.FERMENTED_SPIDER_EYE)  .price(3).build())
        items.add(PriceableItemStackBuilder(Material.GOLDEN_CARROT)         .price(3).build())
        items.add(PriceableItemStackBuilder(Material.SPECKLED_MELON)        .price(2).build())
        items.add(PriceableItemStackBuilder(Material.RABBIT_FOOT)           .price(5).build())
    }
}