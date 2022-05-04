package net.recraft.annihilatoin.objects.map

import net.recraft.annihilatoin.objects.Game
import org.bukkit.Location
import org.bukkit.Material

class EnderFurnace (location: Location) :Placeable(location, Material.FURNACE) {
    companion object {
        fun isIn(location: Location):Boolean {
            for (team in Game.teams) {
                if (team.enderFurnace.location == location) return true
            }
            return false
        }
    }
}