package net.recraft.annihilatoin.objects.boss

import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.objects.GameTeam
import org.bukkit.Bukkit
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.scheduler.BukkitRunnable

abstract class Boss(private val type: EntityType) {
    private val theEnd = Bukkit.getWorld("world_the_end")
//    val location = Location(theEnd, 100.0,100.0,100.0)
    fun spawn(): Entity {
        val entity = GameTeam.GREEN.objects.nexus.location.world.spawnEntity(GameTeam.GREEN.objects.nexus.location, type)
        val later = object: BukkitRunnable() {
            override fun run() {
                entity.teleport(GameTeam.GREEN.objects.nexus.location)
            }
        }
        later.runTaskTimer(Game.plugin, 0, 1)
        return entity
    }
}