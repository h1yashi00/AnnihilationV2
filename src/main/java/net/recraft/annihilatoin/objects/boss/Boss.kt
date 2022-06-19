package net.recraft.annihilatoin.objects.boss

import net.recraft.annihilatoin.util.Util
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity

abstract class Boss(private val type: EntityType) {
    private val theEnd = Bukkit.getWorld("world_the_end")
    val spawnLocation = Location(theEnd, 0.0,65.0, 0.0)
    lateinit var livingEntity: LivingEntity
    fun isBoss(_entity: Entity): Boolean {
        return _entity.uniqueId == livingEntity.uniqueId
    }
    fun spawn() {
        val entity = theEnd.spawnEntity(spawnLocation, type)
        livingEntity = entity as LivingEntity
    }
}