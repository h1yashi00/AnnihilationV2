package net.recraft.annihilatoin.objects.menu

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

class AnniConfigMenu {
    companion object {
        const val title = "Anni Map Config"
        private val specialLore = ArrayList<String>().apply { add("${ChatColor.GOLD}${ChatColor.BOLD}SpecialWool") }
        val redWool   = ItemStack(Material.WOOL,1, 14.toShort()) .apply { val meta = itemMeta; meta.lore = specialLore; setItemMeta(meta)}
        val blueWool  = ItemStack(Material.WOOL,1, 11.toShort()) .apply { val meta = itemMeta; meta.lore = specialLore; setItemMeta(meta)}
        val yellowWool= ItemStack(Material.WOOL,1,  4.toShort()) .apply { val meta = itemMeta; meta.lore = specialLore; setItemMeta(meta)}
        val greenWool = ItemStack(Material.WOOL,1, 13.toShort()) .apply { val meta = itemMeta; meta.lore = specialLore; setItemMeta(meta)}
        val shopBrewing = ItemStack(Material.SIGN)       .apply { val meta = itemMeta; meta.lore = ArrayList<String>().apply { add(title); add("shopbrewing") }; setItemMeta(meta)}
        val shopWeapon= ItemStack(Material.SIGN)       .apply { val meta = itemMeta; meta.lore = ArrayList<String>().apply { add(title); add("shopweapon") }; setItemMeta(meta)}
        val nexus       = ItemStack(Material.ENDER_STONE).apply { val meta = itemMeta; meta.lore = ArrayList<String>().apply { add(
            title
        ) }; setItemMeta(meta)}
        val spawn1      = ItemStack(Material.WOOL)       .apply { val meta = itemMeta; meta.lore = ArrayList<String>().apply { add(
            title
        ); add("spawn1") }; setItemMeta(meta)}
        val spawn2      = ItemStack(Material.WOOL)       .apply { val meta = itemMeta; meta.lore = ArrayList<String>().apply { add(
            title
        ); add("spawn2") }; setItemMeta(meta)}
        val spawn3      = ItemStack(Material.WOOL)       .apply { val meta = itemMeta; meta.lore = ArrayList<String>().apply { add(
            title
        ); add("spawn3") }; setItemMeta(meta)}
        val enderchest  = ItemStack(Material.ENDER_CHEST).apply { val meta = itemMeta; meta.lore = ArrayList<String>().apply { add(
            title
        ) }; setItemMeta(meta)}
        val enderfurnace= ItemStack(Material.FURNACE)    .apply { val meta = itemMeta; meta.lore = ArrayList<String>().apply { add(
            title
        ) }; setItemMeta(meta)}
        fun showMenu(player: Player) {
            val inventory = createMenu()
            player.openInventory(inventory)
        }
        fun isSpecialWool(itemStack: ItemStack): Boolean {
            return when(itemStack) {
                redWool     -> true
                blueWool    -> true
                yellowWool  -> true
                greenWool   -> true
                shopBrewing   -> true
                shopWeapon  -> true
                nexus       -> true
                spawn1      -> true
                spawn2      -> true
                spawn3      -> true
                enderchest  -> true
                enderfurnace -> true
                else    -> false
            }
        }
        private fun createMenu(): Inventory {
            val inventory = Bukkit.createInventory(null, 9, title)
            inventory.setItem(0, redWool)
            inventory.setItem(1, blueWool)
            inventory.setItem(2, yellowWool)
            inventory.setItem(3, greenWool)
            return inventory
        }
        fun giveSpecificItems(colorWool: ItemStack, player: Player) {
            player.inventory.clear()
            player.inventory.setItem(0,colorWool)
            player.inventory.setItem(1, shopWeapon)
            player.inventory.setItem(2, shopBrewing)
            player.inventory.setItem(3, nexus)
            player.inventory.setItem(4, spawn1)
            player.inventory.setItem(5, spawn2)
            player.inventory.setItem(6, spawn3)
            player.inventory.setItem(7, enderchest)
            player.inventory.setItem(8, enderfurnace)
        }
    }
}
