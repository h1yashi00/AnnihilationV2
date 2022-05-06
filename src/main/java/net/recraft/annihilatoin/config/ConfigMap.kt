package net.recraft.annihilatoin.config

import net.recraft.annihilatoin.objects.GameTeam
import net.recraft.annihilatoin.util.GameGenerator
import org.bukkit.*
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.generator.ChunkGenerator
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


class ConfigMap(_file: File): ConfigBase(_file, "maps.yaml") {
    companion object {
        val shopWeapon = "shopweapon"
        val shopBrewing  = "shopbrewing"
        val nexus      = "nexus"
        val spawn1      = "spawn1"
        val spawn2      = "spawn2"
        val spawn3      = "spawn3"
        val enderFurnace = "enderfurnace"
        val enderChest   = "enderchest"
    }
    fun update() {
        fileConfig = YamlConfiguration.loadConfiguration(file)
    }
    fun write(world: World, saveObject: String, team: GameTeam, value: String) {
        val section = fileConfig.getConfigurationSection(world.name)
        section.set("$saveObject.${team.name}", value)
        fileConfig.save(file)
    }
    fun getTeamGenerator(worldName: String): GameGenerator {
        val map = makeWorld(worldName)
        val lobby = makeWorld("world_lobby")
        // maps.yamlがない状態で起動すると
        val section = fileConfig.getConfigurationSection(worldName) ?: addDefaultConfigValues(worldName)!!
        fileConfig.options().copyDefaults(true)
        fileConfig.save(file)
        return GameGenerator(lobby, map, section)
    }

    private fun makeWorld(nameWorld: String): World {
        val wc = WorldCreator(nameWorld)
        wc.generator(object : ChunkGenerator() {
            override fun generate(world: World?, random: Random?, x: Int, z: Int): ByteArray? {
                return ByteArray(32768) //Empty byte array
            }
        })
        Bukkit.createWorld(wc)
        return Bukkit.getWorld(nameWorld)
    }
    private fun addDefaultConfigValues(mapName: String): ConfigurationSection? {
        fileConfig.createSection("$mapName.$shopWeapon")
        fileConfig.createSection("$mapName.$shopBrewing")
        fileConfig.createSection("$mapName.$nexus")
        fileConfig.createSection("$mapName.$spawn1")
        fileConfig.createSection("$mapName.$spawn2")
        fileConfig.createSection("$mapName.$spawn3")
        fileConfig.createSection("$mapName.$enderChest")
        fileConfig.createSection("$mapName.$enderFurnace")
        val teams = ArrayList<String>() .apply { add("red"); add("blue"); add("yellow"); add("green") }
        teams.forEach {
            var section = fileConfig.getConfigurationSection("$mapName.$shopWeapon")
            section.addDefault(it, "0,0,0")
            section = fileConfig.getConfigurationSection("$mapName.$shopBrewing")
            section.addDefault(it, "0,0,0")
            section = fileConfig.getConfigurationSection("$mapName.$nexus")
            section.addDefault(it, "0,0,0")
            section = fileConfig.getConfigurationSection("$mapName.$spawn1")
            section.addDefault(it, "0,0,0")
            section = fileConfig.getConfigurationSection("$mapName.$spawn2")
            section.addDefault(it, "0,0,0")
            section = fileConfig.getConfigurationSection("$mapName.$spawn3")
            section.addDefault(it, "0,0,0")
            section = fileConfig.getConfigurationSection("$mapName.$enderChest")
            section.addDefault(it, "0,0,0")
            section = fileConfig.getConfigurationSection("$mapName.$enderFurnace")
            section.addDefault(it, "0,0,0")
        }
        return fileConfig.getConfigurationSection(mapName)
    }
}
