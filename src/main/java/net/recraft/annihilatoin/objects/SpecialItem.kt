package net.recraft.annihilatoin.objects

import net.recraft.annihilatoin.objects.builder.PriceableItemStackBuilder
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class SpecialItem {
    companion object {
        private val title = "${ChatColor.GOLD}TransPort Item!?"
        fun isTeleportItem(item: ItemStack?): Boolean {
            if (item == null) return false
            if (item.type != Material.QUARTZ) return false
            if (item.itemMeta.displayName != title) return false
            return true
        }
        val item = PriceableItemStackBuilder(Material.QUARTZ).price(3).title(title).build()
    }
}