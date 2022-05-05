package net.recraft.annihilatoin.objects.menu

import org.bukkit.Bukkit
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

abstract class Menu(val title: String) {
    protected val items: MutableList<ItemStack> = ArrayList()
    protected var inventorySize = 9 * 3
    open fun createInventory(): Inventory {
        val inventory = Bukkit.createInventory(null, inventorySize, title)!!
        items.forEach { inventory.addItem(it) }
        return inventory
    }
}