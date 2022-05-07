package net.recraft.annihilatoin.util

import net.minecraft.server.v1_8_R3.NBTTagCompound
import net.recraft.annihilatoin.objects.*
import org.bukkit.*
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack


object Util {
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

    fun getColoredTeamName(team: GameTeam): String {
        return "${team.chatColor}${team.name}${ChatColor.WHITE}"
    }
    fun getColoredPlayersName(player: Player, team: GameTeam): String {
        return "${team.chatColor}${player.name}${ChatColor.WHITE}"
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
        println("aljfdlsajfasdlflkasdj")
    }
}