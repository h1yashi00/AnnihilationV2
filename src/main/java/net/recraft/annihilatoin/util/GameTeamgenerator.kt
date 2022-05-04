package net.recraft.annihilatoin.util

import net.recraft.annihilatoin.objects.GameTeam
import net.recraft.annihilatoin.objects.map.*
import net.recraft.annihilatoin.config.ConfigMap
import org.bukkit.*
import org.bukkit.configuration.ConfigurationSection


class GameGenerator(private val lobby: World, private val map: World, private val section: ConfigurationSection){
    fun getLobby(): World {
        return lobby
    }
    fun getMap(): World {
        return map
    }
    fun getRed(): GameTeam {
        return teamGenerator(Color.RED, ChatColor.RED)
    }
    fun getBlue(): GameTeam {
        return teamGenerator(Color.BLUE, ChatColor.BLUE)
    }
    fun getYellow(): GameTeam {
        return teamGenerator(Color.YELLOW, ChatColor.YELLOW)
    }
    fun getGreen(): GameTeam {
        return teamGenerator(Color.GREEN, ChatColor.GREEN)
    }

    private fun teamGenerator(color: Color, chatColor: ChatColor): GameTeam{
        val name = chatColor.name.lowercase()
        val shopWeapon = shopWeapon(name)
        val shopArmor  = shopArmor(name)
        val nexus  = nexus(name)
        val spawn1 = spawn1(name)
        val spawn2 = spawn2(name)
        val spawn3 = spawn3(name)
        val enderChest   = enderChest(name)
        val enderFurnace = enderFurnace(name)
        return GameTeam(color, chatColor, shopWeapon, shopArmor, nexus, spawn1, spawn2, spawn3, enderChest, enderFurnace)
    }

    private fun shopWeapon(team: String) : ShopWeapon {
        val loc = loadLoc(team, ConfigMap.shopWeapon)
        return ShopWeapon(loc)
    }
    private fun shopArmor(team: String) : ShopArmor {
        val loc = loadLoc(team, ConfigMap.shopArmor)
        return ShopArmor(loc)
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