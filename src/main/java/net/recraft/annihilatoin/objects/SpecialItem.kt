package net.recraft.annihilatoin.objects

import net.recraft.annihilatoin.objects.builder.PriceableItemStackBuilder
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack


class SpecialItem {
    companion object {
        const val handicapCapCoolDown = 20
        private val title = "${ChatColor.GOLD}TransPort Item!?"
        fun isTeleportItem(item: ItemStack?): Boolean {
            if (item == null) return false
            if (item.type != Material.QUARTZ) return false
            if (item.itemMeta.displayName != title) return false
            return true
        }
        // 設置できる最大の距離
        const val maxDisntance = 180
        const val maxHight     = 80
        val item = createItem()
        private fun createItem(): ItemStack {
            return PriceableItemStackBuilder(Material.QUARTZ)
                .price(3)
                .title(title)
                .lore(listOf(
                    "制限付きのTPスニークしながら右クリックで設置可能(消費アイテム)",
                    "自軍との距離が${maxDisntance}離れていると設置できない(ユークリッド距離)",
                    "最高高度は${maxHight}以下",
                    "設置者は${handicapCapCoolDown}秒使用できません"
                ))
                .build()
        }
    }
}