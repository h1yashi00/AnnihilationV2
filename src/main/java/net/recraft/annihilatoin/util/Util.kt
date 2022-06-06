package net.recraft.annihilatoin.util

import net.minecraft.server.v1_8_R3.NBTTagCompound
import net.recraft.annihilatoin.objects.*
import org.bukkit.*
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.generator.ChunkGenerator
import org.bukkit.inventory.ItemStack
import java.nio.file.Paths
import java.util.*
import kotlin.io.path.absolutePathString


object Util {
    fun euclideanDistance(from: Location, to: Location): Double {
        val x1 = from.x
        val x2 = to.x
        val y1  = from.z
        val y2  = to.z
        return Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1))
    }
    fun fatal(msg: String) {
        Bukkit.broadcastMessage("${ChatColor.RED} fatal error in $msg")
        Exception().printStackTrace()
    }
    fun parseLocation(world: World, input: String): Location {
        val params = input.split(",")
        params.forEach {
            it.replace("-0", "0")
        }
        if (params.size != 3) fatal("length is not 3");
        val x = Integer.parseInt(params[0])
        val y = Integer.parseInt(params[1])
        val z = Integer.parseInt(params[2])
        return Location(world, x.toDouble(),y.toDouble(),z.toDouble())
    }

    fun broadcast(msg: String) {
        Bukkit.getOnlinePlayers().forEach { it.sendMessage(msg) }
    }

    fun getColoredTeamName(team: GameTeam): String {
        return "${team.chatColor}${team.name.capitalize()}${ChatColor.WHITE}"
    }
    fun getColoredPlayersName(player: Player, team: GameTeam): String {
        return "${team.chatColor}${player.name}${ChatColor.WHITE}"
    }

    fun getItemDurability(item: ItemStack): Int {
        return (item.type.maxDurability - item.durability)
    }
    fun setItemDurability(item: ItemStack, num: Int) {
        item.durability = (item.type.maxDurability - num).toShort()
    }

    fun isLog(material: Material):Boolean {
        return material == Material.LOG || material == Material.LOG_2;
    }

    fun isAir(item: ItemStack): Boolean {
        return item.type == Material.AIR
    }
    // TODO 何故か動かない
    fun removeAI(entity: Entity) {
        val nmsEnt: net.minecraft.server.v1_8_R3.Entity   = (entity as CraftEntity).handle
        val tag: NBTTagCompound = nmsEnt.nbtTag ?:NBTTagCompound()
        tag.setInt("NoAI", 0)
        nmsEnt.c(tag)
        nmsEnt.f(tag)
    }
    fun dontStack(item: ItemStack): ItemStack {
        val stack = CraftItemStack.asNMSCopy(item)
        val tag = stack.tag ?: NBTTagCompound()
        tag.setString("random-id", UUID.randomUUID().toString())
        stack.setTag(tag)
        return CraftItemStack.asBukkitCopy(stack)
    }

    fun copyWorld(worldName: String) {
        val serverMapFile = Paths.get(getServerPath()+"/$worldName")
        val savedMapFile= Paths.get(Game.plugin.dataFolder.path + "/worlds/"+worldName)
        serverMapFile.toFile().deleteRecursively()
        broadcast(savedMapFile.toFile().copyRecursively(serverMapFile.toFile()).toString())
    }

    private fun getServerPath(): String {
        return Paths.get(Game.plugin.dataFolder.absolutePath).parent.parent.absolutePathString()
    }

    fun makeWorld(nameWorld: String): World {
        val wc = WorldCreator(nameWorld)
        wc.generator(object : ChunkGenerator() {
            override fun generate(world: World?, random: Random?, x: Int, z: Int): ByteArray? {
                return ByteArray(32768) //Empty byte array
            }
        })
        Bukkit.createWorld(wc)
        return Bukkit.getWorld(nameWorld)
    }
    fun isUnderVoid(location: Location): Boolean {
        val world = location.world
        var y = location.y
        while(0 <= y) {
            val checkLoc = Location(world, location.x, y, location.z)
            if (checkLoc.block.type != Material.AIR) return false
            y -= 1
        }
        return true
    }
}