package net.recraft.annihilatoin.objects

import net.recraft.annihilatoin.objects.map.*
import org.bukkit.ChatColor
import org.bukkit.Color
import java.util.*

class GameTeam (val color: Color,
                val chatColor: ChatColor,
                val shopWeapon:ShopWeapon,
                val shopArmor: ShopArmor,
                val nexus:  Nexus,
                val spawn1: Spawn,
                val spawn2: Spawn,
                val spawn3: Spawn,
                val enderChest: EnderChest,
                val enderFurnace: EnderFurnace)
{
    val name
        get() = chatColor.name.lowercase()
    private val mate: MutableSet<UUID> = HashSet()
    val prefix: String
        get()  {
            val prefixTeamIcon = "[${color}${name[0]}${ChatColor.WHITE}]"
            return "${prefixTeamIcon}${color}"
        }

    fun place() {
        shopWeapon  .place()
        shopArmor   .place()
        nexus       .place()
        spawn1      .place()
        spawn2      .place()
        spawn3      .place()
        enderChest  .place()
        enderFurnace.place()
    }
    fun remove(uuid: UUID) {
        mate.remove(uuid)
    }
    fun join(uuid: UUID) {
        mate.add(uuid)
    }
    operator fun contains(uuid: UUID): Boolean {
        return uuid in mate
    }
}