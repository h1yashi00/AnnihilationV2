package net.recraft.annihilatoin.admin

import net.recraft.annihilatoin.objects.builder.ItemStackBuilder
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class AnniConfigMenu {
    val title = "Anni Map Config"

    private class AnniConfigMenuItemStackBuilder(): ItemStackBuilder(Material.WOOL) {
        private val specialLore = ArrayList<String>().apply { add("${ChatColor.GOLD}${ChatColor.BOLD}SpecialWool") }
        override fun build(): ItemStack {
            lore(specialLore)
            return super.build()
        }
    }
    enum class Menu(val item: ItemStack) {
        RED(AnniConfigMenuItemStackBuilder().damage(14).build()),
        BLUE(AnniConfigMenuItemStackBuilder().damage(11).build()),
        YELLOW(AnniConfigMenuItemStackBuilder().damage(4).build()),
        GREEN (AnniConfigMenuItemStackBuilder().damage(13).build());
        companion object {
            fun isSpecialWool(item: ItemStack): Boolean {
                values().forEach {
                    if (it.item == item) return true
                }
                return false
            }
        }
    }

    fun showMenu(player: Player) {
        val inventory = createMenu()
        player.openInventory(inventory)
    }

    private fun createMenu(): Inventory {
        val inventory = Bukkit.createInventory(null, 9, title)
        Menu.values().forEach {
            inventory.addItem(it.item)
        }
        return inventory
    }
}
