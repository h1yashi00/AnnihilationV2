package net.recraft.annihilatoin.objects.kit

import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory


class Scorpio: KitBase(
    KitType.SCORPIO,
    Material.NETHER_STAR,
    listOf(
        "ネザースターを持って右クリックをすると投げることができる",
        "それを敵当たると目の前に引き寄せる事ができる｡"
    )
) {
    companion object {
        val displayName = "${ChatColor.YELLOW}Hook"
        fun isScorpioItem(item: ItemStack): Boolean {
            if (item.type != Material.NETHER_STAR) return false
            if (item.itemMeta.displayName != displayName) return false
            return true
        }
    }

    override fun setItems(playerInventory: PlayerInventory) {
        super.setItems(playerInventory)
        playerInventory.setItem(3, getScorpioItem())
    }
    private fun getScorpioItem(): ItemStack {
        val scorpio = ItemStack(Material.NETHER_STAR)
        val meta = scorpio.itemMeta
        meta.displayName = displayName
        scorpio.itemMeta = meta
        return scorpio
    }
    override fun setInit(player: Player) {
    }
}