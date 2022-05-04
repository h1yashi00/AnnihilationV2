package net.recraft.annihilatoin.util

import net.recraft.annihilatoin.objects.*
import org.bukkit.*
import org.bukkit.block.Block
import org.bukkit.entity.Player
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


    fun getStatus(block: Block):ResourceBlocks? {
        for (resource in ResourceBlocks.values()) {
            if (isLog(block.type)) {
                return getResourceLog(block)
            }
            if (resource.type == block.type) {
                return resource
            }
        }
        return null
    }
    // 原木はMaterial.Log Log_2で管理されている
    private fun getResourceLog(log: Block): ResourceBlocks? {
        when (log.type) {
            Material.LOG -> {
                when (log.data % 4) {
                    0 -> return ResourceBlocks.OAK
                    1 -> return ResourceBlocks.SPRUCE
                    2 -> return ResourceBlocks.BIRCH
                    3 -> return ResourceBlocks.JUNGLE
                    else -> fatal("Is Log?")
                }
            }
            Material.LOG_2 -> {
                when (log.data % 4) {
                    0 -> return ResourceBlocks.ACACIA
                    1 -> return ResourceBlocks.DARK_OAK
                    else -> fatal("IS LOG_2?")
                }
            }
            else -> {
                fatal("IS not LOG or LOG_2")
            }
        }
        return null
    }

    fun isLog(material: Material):Boolean {
        return material == Material.LOG || material == Material.LOG_2;
    }
}