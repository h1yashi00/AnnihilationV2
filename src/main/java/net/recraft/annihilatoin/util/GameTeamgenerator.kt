package net.recraft.annihilatoin.util

import com.sk89q.worldedit.bukkit.selections.CuboidSelection
import com.sk89q.worldedit.bukkit.selections.Selection
import net.recraft.annihilatoin.objects.map.GameTeamObjects
import net.recraft.annihilatoin.objects.map.*
import net.recraft.annihilatoin.config.ConfigMap
import net.recraft.annihilatoin.objects.GameTeam
import org.bukkit.*
import org.bukkit.configuration.ConfigurationSection
import net.recraft.annihilatoin.config.ConfigMap.MapObjective.*


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

    fun getMapObject(): MapObject {
        return gameMapGenerator()
    }

    private fun gameMapGenerator(): MapObject {
        return MapObject(bossGate1(), bossGate2(), mapRange())
    }

    private fun bossGate1(): CuboidSelection {
        val min = loadLoc(POS.MIN, BOSS_GATE1)
        val max = loadLoc(POS.MAX, BOSS_GATE1)
        return CuboidSelection(map, min, max)
    }
    private fun bossGate2(): CuboidSelection {
        val min = loadLoc(POS.MIN, BOSS_GATE2)
        val max = loadLoc(POS.MAX, BOSS_GATE2)
        return CuboidSelection(map, min, max)
    }

    private fun mapRange(): CuboidSelection {
        val min = loadLoc(POS.MIN, MAP_RANGE)
        val max = loadLoc(POS.MAX, MAP_RANGE)
        return CuboidSelection(map, min, max)
    }


    private fun teamGenerator(team: GameTeam): GameTeamObjects {
        val name = team.toString()
        val shopWeapon = shopWeapon(name)
        val shopBrewing  = shopBrewing(name)
        val nexus  = nexus(name)
        val spawn1 = spawn1(name)
        val spawn2 = spawn2(name)
        val spawn3 = spawn3(name)
        val enderChest   = enderChest(name)
        val enderFurnace = enderFurnace(name)
        val defender     = defender(name)
        val protectBase  = protectBase(name)
        return GameTeamObjects(shopWeapon, shopBrewing, nexus, spawn1, spawn2, spawn3, enderChest, enderFurnace, defender, protectBase)
    }

    private fun protectBase(team: String): Selection {
        val min = loadLoc(team, ConfigMap.minProtectBase)
        val max = loadLoc(team, ConfigMap.maxProtectBase)
        return CuboidSelection(map, min, max)
    }

    private fun defender(team: String): Location {
        return loadLoc(team, ConfigMap.TeamObjective.DEFENDER)
    }

    private fun shopWeapon(team: String) : ShopWeapon {
        val loc = loadLoc(team, ConfigMap.TeamObjective.SHOP_WEAPON)
        return ShopWeapon(loc)
    }
    private fun shopBrewing(team: String) : ShopBrewing {
        val loc = loadLoc(team, ConfigMap.TeamObjective.SHOP_BREWING)
        return ShopBrewing(loc)
    }
    private fun spawn1(team: String) : Spawn {
        val loc = loadLoc(team, ConfigMap.TeamObjective.SPAWN1)
        return Spawn(loc)
    }
    private fun spawn2(team: String) : Spawn {
        val loc = loadLoc(team, ConfigMap.TeamObjective.SPAWN2)
        return Spawn(loc)
    }
    private fun spawn3(team: String) : Spawn {
        val loc = loadLoc(team, ConfigMap.TeamObjective.SPAWN3)
        return Spawn(loc)
    }
    private fun nexus(team: String) : Nexus {
        val loc = loadLoc(team, ConfigMap.TeamObjective.NEXUS)
        return Nexus(loc)
    }
    private fun enderChest(team: String) : EnderChest {
        val loc = loadLoc(team, ConfigMap.TeamObjective.ENDER_CHEST)
        return EnderChest(loc)
    }
    private fun enderFurnace(team: String) : EnderFurnace {
        val loc = loadLoc(team, ConfigMap.TeamObjective.ENDER_FURNACE)
        return EnderFurnace(loc)
    }

    private fun loadLoc(pos: ConfigMap.MapObjective.POS, key: ConfigMap.MapObjective): Location {
        return loadLoc(pos.toString(), key.toString())
    }

    private fun loadLoc(team: String, key: ConfigMap.TeamObjective): Location {
        return loadLoc(team, key.toString())
    }

    private fun loadLoc(team: String, key: String): Location {
        if (!section.contains("$key.$team")) Util.fatal("$team in maps.yaml can not find $key")
        return Util.parseLocation(map, section.getString("$key.$team"))
    }

}