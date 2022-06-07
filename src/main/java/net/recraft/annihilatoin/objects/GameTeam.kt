package net.recraft.annihilatoin.objects

import net.recraft.annihilatoin.objects.map.GameTeamObjects
import net.recraft.annihilatoin.objects.map.Nexus
import org.bukkit.ChatColor
import org.bukkit.Color
import org.bukkit.Location

enum class GameTeam(
    val color: Color,
    val chatColor: ChatColor
) {
    RED     (Color.RED, ChatColor.RED),
    BLUE    (Color.BLUE, ChatColor.BLUE),
    YELLOW  (Color.YELLOW, ChatColor.YELLOW),
    GREEN   (Color.GREEN, ChatColor.GREEN);
    override fun toString(): String {
        return name.lowercase()
    }
    companion object {
        fun isShopBrewing(location: Location): Boolean {
            if (!Game.isStart) return false
            values().forEach {
                if (it.objects.shopBrewing.location == location) return true
            }
            return false
        }
        fun isShopWeapon(location: Location): Boolean {
            if (!Game.isStart) return false
            values().forEach {
                if (it.objects.shopWeapon.location == location) return true
            }
            return false
        }
        fun isEnderChest(location: Location): Boolean {
            if (!Game.isStart) return false
            values().forEach {
                if (it.objects.enderChest.location == location) return true
            }
            return false
        }
        fun isEnderFurnace(location: Location): Boolean {
            if (!Game.isStart) return false
            values().forEach {
                if (it.objects.enderFurnace.location == location) return true
            }
            return false
        }
        fun isFinishStatus():Boolean {
            var i = 0
            values().forEach {
                if (it.objects.nexus.isAlive()) {
                    i+=1
                }
            }
            if (i == 1) {
                return true
            }
            return false
        }
        fun getTeam(color: String): GameTeam? {
            values().forEach {
                if (it.toString() == color) return it
            }
            return null
        }
        fun getTeam(nexus: Nexus): GameTeam? {
            values().forEach {
                if (it.objects.nexus == nexus) return it
            }
            return null
        }
        fun getNexus(location: Location): Nexus? {
            values().forEach {
                if (it.objects.nexus.location == location) return it.objects.nexus
            }
            return null
        }
    }
    lateinit var objects: GameTeamObjects
    val prefix: String
        get()  {
            val prefixTeamIcon = "[${chatColor}${name[0]}${ChatColor.WHITE}]"
            return "${prefixTeamIcon}${chatColor}"
        }
}