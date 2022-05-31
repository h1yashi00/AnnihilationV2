package net.recraft.annihilatoin.objects.kit

import net.recraft.annihilatoin.objects.builder.ItemStackBuilder
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory

class Defender: KitBase(
    KitType.DEFENDER,
    ItemStack(Material.INK_SACK,1, 9),
    listOf(
        "Kitアイテムを右クリックするとネクサス付近にテレポートできる",
        "ネクサスの体力に応じて体力が上昇､恒久的に再生1が付与される｡",
        "拠点から離れるとスキルは消滅する"
    )
) {
    companion object {
        val title = "${ChatColor.GOLD}Guardian"
        val maxDistance = 80
        fun isItem(item: ItemStack?): Boolean {
            if (item == null) return false
            val meta = item.itemMeta ?: return false
            if (meta.displayName == title) return true
            return false
//            if (meta.lore == null) return false
//            meta.lore.forEach { if (it == title) return true }
        }

        private fun getItem(): ItemStack {
            return ItemStackBuilder(Material.INK_SACK)
                .damage(9)
                .title(title)
                .build()
        }
    }
    override fun setItems(playerInventory: PlayerInventory) {
        super.setItems(playerInventory)
        playerInventory.setItem(3, getItem())
    }
}