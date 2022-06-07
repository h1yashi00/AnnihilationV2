package net.recraft.annihilatoin.objects.map

import com.sk89q.worldedit.bukkit.selections.Selection
import org.bukkit.Location

class GameTeamObjects (
                   val shopWeapon:ShopWeapon,
                   val shopBrewing: ShopBrewing,
                   val nexus:  Nexus,
                   val spawn1: Spawn,
                   val spawn2: Spawn,
                   val spawn3: Spawn,
                   val enderChest: EnderChest,
                   val enderFurnace: EnderFurnace,
                   val defender: Location,
                   val base: Selection
)
{
    val randomSpawn get() = listOf(spawn1, spawn2, spawn3).shuffled().first().location.clone().apply {y += 1}
    fun place() {
        shopWeapon  .place()
        shopBrewing .place()
        nexus       .place()
        spawn1      .place()
        spawn2      .place()
        spawn3      .place()
        enderChest  .place()
        enderFurnace.place()
    }
}