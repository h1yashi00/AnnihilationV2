package net.recraft.annihilatoin.util

import net.recraft.annihilatoin.objects.map.GameTeamObjects
import net.recraft.annihilatoin.objects.map.*
import net.recraft.annihilatoin.config.ConfigMap
import net.recraft.annihilatoin.objects.GameTeam
import org.bukkit.*
import org.bukkit.configuration.ConfigurationSection


class GameGenerator(private val map: World, private val section: ConfigurationSection){
    fun getMap(): World {
        return map
    }
    fun getRed(): GameTeamObjects {
        return teamGenerator(GameTeam.RED)
    }
    fun getBlue(): GameTeamObjects {
        return teamGenerator(GameTeam.BLUE)
    }
    fun getYellow(): GameTeamObjects {
        return teamGenerator(GameTeam.YELLOW)
    }
    fun getGreen(): GameTeamObjects {
        return teamGenerator(GameTeam.GREEN)
    }

    private fun teamGenerator(team: GameTeam): GameTeamObjects {
        val name = team.name.lowercase()
        val shopWeapon = shopWeapon(name)
        val shopBrewing  = shopBrewing(name)
        val nexus  = nexus(name)
        val spawn1 = spawn1(name)
        val spawn2 = spawn2(name)
        val spawn3 = spawn3(name)
        val enderChest   = enderChest(name)
        val enderFurnace = enderFurnace(name)
        return GameTeamObjects(shopWeapon, shopBrewing, nexus, spawn1, spawn2, spawn3, enderChest, enderFurnace)
    }

    private fun shopWeapon(team: String) : ShopWeapon {
        val loc = loadLoc(team, ConfigMap.shopWeapon)
        return ShopWeapon(loc)
    }
    private fun shopBrewing(team: String) : ShopBrewing {
        val loc = loadLoc(team, ConfigMap.shopBrewing)
        return ShopBrewing(loc)
    }
    private fun spawn1(team: String) : Spawn {
        val loc = loadLoc(team, ConfigMap.spawn1)
        return Spawn(loc)
    }
    private fun spawn2(team: String) : Spawn {
        val loc = loadLoc(team, ConfigMap.spawn2)
        return Spawn(loc)
    }
    private fun spawn3(team: String) : Spawn {
        val loc = loadLoc(team, ConfigMap.spawn3)
        return Spawn(loc)
    }
    private fun nexus(team: String) : Nexus {
        val loc = loadLoc(team, ConfigMap.nexus)
        return Nexus(loc)
    }
    private fun enderChest(team: String) : EnderChest {
        val loc = loadLoc(team, ConfigMap.enderChest)
        return EnderChest(loc)
    }
    private fun enderFurnace(team: String) : EnderFurnace {
        val loc = loadLoc(team, ConfigMap.enderFurnace)
        return EnderFurnace(loc)
    }


    private fun loadLoc(team: String, key: String): Location {
        if (!section.contains("$key.$team")) Util.fatal("$team in maps.yaml can not find $key")
        return Util.parseLocation(map, section.getString("$key.$team"))
    }
}