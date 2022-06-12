package net.recraft.annihilatoin.config

import net.recraft.annihilatoin.objects.GameTeam
import net.recraft.annihilatoin.objects.builder.ItemStackBuilder
import net.recraft.annihilatoin.util.GameGenerator
import net.recraft.annihilatoin.util.Util
import org.bukkit.*
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.io.File

class ConfigMap(_file: File): ConfigBase(_file, "maps.yaml") {
    private class AnniConfigItemStackBuilder(material: Material): ItemStackBuilder(material)  {
        override fun build(): ItemStack {
            title("Anni Map Config")
            return super.build()
        }
    }
    enum class Objective(val item: ItemStack) {
        SHOP_WEAPON(AnniConfigItemStackBuilder(Material.SIGN).lore(listOf("shopweapon")).build()),
        SHOP_BREWING(AnniConfigItemStackBuilder(Material.SIGN).lore(listOf("shopbrewing")).build()),
        NEXUS(AnniConfigItemStackBuilder(Material.ENDER_STONE).lore(listOf("nexus")).build()),
        SPAWN1(AnniConfigItemStackBuilder(Material.WOOL).lore(listOf("spawn1")).build()),
        SPAWN2(AnniConfigItemStackBuilder(Material.WOOL).lore(listOf("spawn2")).build()),
        SPAWN3(AnniConfigItemStackBuilder(Material.WOOL).lore(listOf("spawn3")).build()),
        ENDER_CHEST(AnniConfigItemStackBuilder(Material.ENDER_CHEST).lore(listOf("enderchest")).build()),
        ENDER_FURNACE(AnniConfigItemStackBuilder(Material.FURNACE).lore(listOf("enderfurnace")).build()),
        DEFENDER(AnniConfigItemStackBuilder(Material.ENDER_PEARL).lore(listOf("defender")).build()),
        PROTECT_BASE(AnniConfigItemStackBuilder(Material.STAINED_GLASS).damage(14).lore(listOf("protectbase")).build());

        override fun toString(): String {
            return name.toLowerCase()
        }

        companion object {
            fun getObject(item: ItemStack?): Objective? {
                values().forEach {
                    if (it.item == item) return it
                }
                return null
            }
            fun giveSpecificItems(colorWool: ItemStack, player: Player) {
                player.inventory.clear()
                player.inventory.addItem(colorWool)
                values().forEach {
                    player.inventory.addItem(it.item)
                }
            }
        }
    }
    fun update() {
        fileConfig = YamlConfiguration.loadConfiguration(file)
    }
    companion object {
        val minProtectBase = "min_protect_base"
        val maxProtectBase = "max_protect_base"
    }
    fun writePlayerSelection(player: Player, team: GameTeam, minLoc: Location, maxLoc: Location) {
        val world = player.world
        val minLocString = toLocString(minLoc)
        write(world, minProtectBase, team, minLocString)
        val maxLocString = toLocString(maxLoc)
        write(world, maxProtectBase, team, maxLocString)
    }
    private fun toLocString(location: Location): String {
        val x = location.x.toInt()
        val y = location.y.toInt()
        val z = location.z.toInt()
        return "$x,$y,$z"
    }
    fun writeLocation(world: World, objective: Objective, team: GameTeam, value: Location) {
        val locString = toLocString(value)
        write(world, objective.toString(), team, locString)
    }
    private fun write(world: World, saveObject: String, team: GameTeam, value: String) {
        val section = fileConfig.getConfigurationSection(world.name)
        section.set("${saveObject.toLowerCase()}.$team", value)
        fileConfig.save(file)
    }
    fun getTeamGenerator(worldName: String): GameGenerator {
        val map = Util.makeWorld(worldName)
        // maps.yamlがない状態で起動すると
        val section = fileConfig.getConfigurationSection(worldName) ?: addDefaultConfigValues(worldName)!!
        fileConfig.options().copyDefaults(true)
        fileConfig.save(file)
        return GameGenerator(map, section)
    }

    private fun addDefaultConfigValues(mapName: String): ConfigurationSection? {
        GameTeam.values().forEach { team ->
            Objective.values().forEach {
                fileConfig.createSection("$mapName.$it")
                val section = fileConfig.getConfigurationSection("$mapName.${it}")
                section.addDefault(team.toString(), "0,0,0")
            }
            fileConfig.createSection("$mapName.$minProtectBase")
            val min = fileConfig.getConfigurationSection("$mapName.$minProtectBase")
            min.addDefault(team.toString(), "0,0,0")

            fileConfig.createSection("$mapName.$maxProtectBase")
            val max = fileConfig.getConfigurationSection("$mapName.$maxProtectBase")
            max.addDefault(team.toString(),"0,0,0")
        }
        return fileConfig.getConfigurationSection(mapName)
    }
}
