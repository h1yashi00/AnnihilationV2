package net.recraft.annihilatoin.objects.kit

import net.recraft.annihilatoin.objects.builder.ItemStackBuilder
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory

class Dasher: KitBase(
    KitType.DASHER,
    Material.ENDER_PEARL,
    listOf(
        "DasherItemをクリックすると指定したブロックにテレポートできます｡(半径25)",
        "CoolDown は移動したブロック/2+3です"
    )) {
    companion object {
        private val title = "${ChatColor.GREEN}Blink"
        fun isKitItem(item: ItemStack?): Boolean {
            if (item == null) return false
            if (item.type != Material.INK_SACK) return false
            if (item.itemMeta.displayName != title) return false
            return true
        }
    }

    override fun setItems(playerInventory: PlayerInventory) {
        super.setItems(playerInventory)
        playerInventory.setItem(3, ItemStackBuilder(Material.INK_SACK).title(title).build())
    }
}