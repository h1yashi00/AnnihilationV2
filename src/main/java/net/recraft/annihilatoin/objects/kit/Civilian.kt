package net.recraft.annihilatoin.objects.kit

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory

class Civilian: KitBase(
    KitType.CIVILIAN,
    ItemStack(Material.PORTAL),
    listOf("石ツール一式を持った序盤万能キット!?")
) {
    override val axe: ItemStack
        get() = ItemStack(Material.STONE_AXE)
    override val pickaxe: ItemStack
        get() = ItemStack(Material.STONE_PICKAXE)
    override val sword: ItemStack
        get() = ItemStack(Material.STONE_SWORD)
    override fun setItems(playerInventory: PlayerInventory) {
        super.setItems(playerInventory)
        playerInventory.setItem(3, ItemStack(Material.STONE_SPADE))
    }
}