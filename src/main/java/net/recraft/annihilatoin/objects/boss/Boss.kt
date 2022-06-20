package net.recraft.annihilatoin.objects.boss

import net.recraft.annihilatoin.objects.Game
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.scheduler.BukkitRunnable

abstract class Boss(private val type: EntityType) {
    val theEnd = Bukkit.getWorld("world_the_end")
    val spawnLocation = Location(theEnd, 0.0,65.0, 0.0)
    var livingEntity: LivingEntity? = null
    fun isBoss(_entity: Entity): Boolean {
        if (livingEntity == null) return false
        return _entity.uniqueId == livingEntity!!.uniqueId
    }
    fun spawn() {
        object: BukkitRunnable() {
            override fun run() {
                val entity = theEnd.spawnEntity(spawnLocation, type)
                livingEntity = entity as LivingEntity
            }
        }.runTaskLater(Game.plugin, 10)
    }
}