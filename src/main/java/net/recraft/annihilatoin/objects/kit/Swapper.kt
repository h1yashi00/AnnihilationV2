package net.recraft.annihilatoin.objects.kit

import net.recraft.annihilatoin.objects.builder.ItemStackBuilder
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory

class Swapper: KitBase(
    KitType.SWAPPER,
    Material.GREEN_RECORD,
    listOf(
        "スキルが発動すると",
        "相手と自分の位置を入れ替える事ができます(最大8ブロック)",
        "相手には衝撃吸収+5が付与されます"
)) {
    companion object {
        val cooldown = 8
        val displayName  = "${ChatColor.YELLOW}SWAPPER"
        fun isSwapItem(item: ItemStack): Boolean {
            if (item.type != Material.GREEN_RECORD) return false
            if (item.itemMeta.displayName != displayName) return false
            return true
        }
        private fun getSwapItem(): ItemStack {
            val creator =  object: ItemStackBuilder(Material.GREEN_RECORD){}
            return creator.title(displayName).build()
        }
    }

    override fun setItems(playerInventory: PlayerInventory) {
        super.setItems(playerInventory)
        playerInventory.setItem(3, getSwapItem())
    }

    override fun setInit(player: Player) {
    }
}