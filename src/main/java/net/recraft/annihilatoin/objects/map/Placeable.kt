package net.recraft.annihilatoin.objects.map

import org.bukkit.Location
import org.bukkit.Material

abstract class Placeable(val location: Location, private val material: Material) {
    open fun place() {
        location.block.type = material
    }
}