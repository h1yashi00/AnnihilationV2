package net.recraft.annihilatoin.objects.map

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Sign

class Shop(location: Location) : Placeable(location, Material.WALL_SIGN){
    override fun place() {
        super.place()
        val block = location.block
        val sign = if (block.type == Material.WALL_SIGN) { block.state as Sign } else { return }
        sign.lines[0] = "[Shop]"
        sign.lines[1] = "${ChatColor.DARK_PURPLE}${ChatColor.BOLD}Weapon"
        sign.lines[2] = "[Shop]"
        sign.lines[3] = "${ChatColor.DARK_PURPLE}${ChatColor.BOLD}Armor"
        sign.update()
    }
}