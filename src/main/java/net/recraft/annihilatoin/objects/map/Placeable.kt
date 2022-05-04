package net.recraft.annihilatoin.objects.map

import org.bukkit.Location
import org.bukkit.Material

abstract class Placeable(val location: Location, private val material: Material) {
    open fun place() {
        val block = location.world.getBlockAt(location)
        if (block.type != material) location.block.type = material
    }
}