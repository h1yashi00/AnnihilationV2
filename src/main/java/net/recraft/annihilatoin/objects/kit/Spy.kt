package net.recraft.annihilatoin.objects.kit

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class Spy: KitBase(
    KitType.SPY,
    ItemStack(Material.POTION,1, 8270),
    listOf("あらゆるものから姿を隠すことができる｡")
) {
}