package net.recraft.annihilatoin.objects.builder

import org.bukkit.inventory.ItemStack

interface Priceable {
    companion object {
        fun getPrice(item: ItemStack):Int {
            val lore = item.itemMeta.lore ?: return 999
            if (lore.size == 0) return 999
            val price = lore[0].split(" ")[1]
            return Integer.valueOf(price)
        }
    }
    var price: Int
}