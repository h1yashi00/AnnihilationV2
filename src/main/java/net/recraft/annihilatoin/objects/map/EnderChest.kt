package net.recraft.annihilatoin.objects.map

import net.recraft.annihilatoin.objects.Game
import org.bukkit.Location
import org.bukkit.Material

class EnderChest (location: Location) : Placeable(location, Material.ENDER_CHEST){
    companion object {
        fun isIn(location: Location):Boolean {
            for (team in Game.teams) {
                if (team.enderChest.location == location) return true
            }
            return false
        }
    }
}