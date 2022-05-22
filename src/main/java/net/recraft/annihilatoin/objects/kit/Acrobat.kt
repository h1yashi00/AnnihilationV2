package net.recraft.annihilatoin.objects.kit

import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory

class Acrobat: KitBase(
    KitType.ACROBAT,
    Material.FEATHER,
    listOf(
        "落下ダメージを受けない",
        "二段ジャンプができる"
    )
) {
    override fun setInit(player: Player) {
        player.allowFlight = true
        player.isFlying = false
    }
    override fun setItems(playerInventory: PlayerInventory) {
        super.setItems(playerInventory)
        playerInventory.setItem(3, ItemStack(Material.BOW))
        playerInventory.setItem(4, ItemStack(Material.ARROW, 8))
    }
}