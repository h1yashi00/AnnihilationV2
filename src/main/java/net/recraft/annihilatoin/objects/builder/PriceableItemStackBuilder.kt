package net.recraft.annihilatoin.objects.builder

import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class PriceableItemStackBuilder(material: Material): ItemStackBuilder(material), Priceable {
    override var price: Int = 999
    fun price(_price: Int): PriceableItemStackBuilder {
        price = _price
        return this
    }

    override fun build(): ItemStack {
        val item = super.build()
        val meta = item.itemMeta
        val lore = meta.lore ?: ArrayList()
        val newLore = lore.apply {add("${ChatColor.GOLD}price: $price")}
        meta.lore = newLore
        item.setItemMeta(meta)
        return item
    }
}