package net.recraft.annihilatoin.listener.boss

import net.recraft.annihilatoin.database.AnnihilationStatsColumn
import net.recraft.annihilatoin.database.Database
import net.recraft.annihilatoin.event.BossSpawnEvent
import net.recraft.annihilatoin.isSame
import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.objects.Game.map
import net.recraft.annihilatoin.objects.Game.team
import net.recraft.annihilatoin.objects.boss.Boss
import net.recraft.annihilatoin.objects.boss.WitherBoss
import net.recraft.annihilatoin.objects.builder.ItemStackBuilder
import net.recraft.annihilatoin.realTeleport
import org.bukkit.*
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.*
import org.bukkit.event.player.PlayerPickupItemEvent
import org.bukkit.event.player.PlayerPortalEvent
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector
import java.util.*

class BossListener: Listener {
    var boss: Boss? = null

    @EventHandler
    fun playerDamage(event: EntityDamageByEntityEvent) {
        if (boss == null) return
        if (boss!!.isBoss(event.entity)) {
            object: BukkitRunnable() {
                override fun run() {
                    event.entity.velocity = Vector()
                }
            }.runTaskLater(Game.plugin, 1)
        }
    }
    @EventHandler
    fun onThroughPortal(event: PlayerPortalEvent) {
        val theEnd = Bukkit.getWorld("world_the_end")
        val player = event.player
        if (event.cause != PlayerTeleportEvent.TeleportCause.END_PORTAL) return
        println("${player.world}")
        if (player.world == theEnd) {
            val team = player.team()
            player.realTeleport(team!!.objects.randomSpawn)
            return
        }
        val listOfLoc = listOf(
            Location(theEnd, 12.0, 68.0,-12.0),
            Location(theEnd, 13.0, 68.0, 12.0),
            Location(theEnd, -11.0, 68.0,13.0),
            Location(theEnd, -13.0, 68.0, -12.0)
        )
        player.realTeleport(listOfLoc.random())
        if (boss == null) return
        if (boss!!.livingEntity == null) {
            boss!!.spawn()
        }
    }

    @EventHandler
    fun bossSpawn(event: BossSpawnEvent) {
        boss = event.boss
        val bossGate1 = Game.mapObject.bossGate1
        bossGate1.regionSelector.region.forEach {
            map.getBlockAt(it.blockX, it.blockY, it.blockZ).type = Material.ENDER_PORTAL
        }
        val bossGate2 = Game.mapObject.bossGate2
        bossGate2.regionSelector.region.forEach {
            map.getBlockAt(it.blockX, it.blockY, it.blockZ).type = Material.ENDER_PORTAL
        }
        Bukkit.getOnlinePlayers().forEach {
            it.playSound(it.location, Sound.WITHER_SPAWN, 1F, 1F)
        }
        val theEnd = event.boss.theEnd
        if (theEnd.entities.isNotEmpty())
            theEnd.entities.forEach {
                if (it is Player) {
                    boss!!.spawn()
                }
        }
        object: BukkitRunnable() {
            override fun run() {
                if (event.boss.livingEntity == null) return
                if (event.boss.livingEntity!!.isDead) cancel()
                var entity: Entity? = null
                // world.spawnEntityから取り出したエンティティだとなぜかteleportできない
                theEnd.entities.forEach { if (boss!!.isBoss(it)) entity = it }
                if (entity == null) return
                theEnd.entities.forEach {
                    if (it is Player) {
                       entity!!.teleport(boss!!.spawnLocation)
                        return
                    }
                }
            }
        }.runTaskTimer(Game.plugin, 0,1)
    }

    @EventHandler
    fun bossDeath(event: EntityDeathEvent) {
        if (boss == null) return
        if (!boss!!.isBoss(event.entity)) return
        val killer = event.entity.killer
        object: BukkitRunnable() {
            override fun run() {
                Database.incCount(AnnihilationStatsColumn.BOSS_KILLS,killer.uniqueId)
            }
        }.runTaskAsynchronously(Game.plugin)
        event.droppedExp = 0
        event.drops.clear()
        val team = killer.team()
        object: BukkitRunnable() {
            override fun run() {
                Bukkit.getOnlinePlayers().forEach {
                    if (it.team() == team) {
                        Database.addCount(AnnihilationStatsColumn.COIN, it.uniqueId, 50)
                        it.sendMessage("${ChatColor.GOLD} +50 Annihilation Coin")
                    }
                }
            }
        }.runTaskAsynchronously(Game.plugin)
        Bukkit.getOnlinePlayers().forEach {
            if (it.team() == team) {
                it.inventory.addItem(BossBuffListener.itemBossBuff)
            }
        }

        val bossGate1 = Game.mapObject.bossGate1
        bossGate1.regionSelector.region.forEach {
            map.getBlockAt(it.blockX, it.blockY, it.blockZ).type = Material.AIR
        }
        val bossGate2 = Game.mapObject.bossGate2
        bossGate2.regionSelector.region.forEach {
            map.getBlockAt(it.blockX, it.blockY, it.blockZ).type = Material.AIR
        }
        object: BukkitRunnable() {
            override fun run() {
                Bukkit.getPluginManager().callEvent(BossSpawnEvent(WitherBoss()))
            }
        }.runTaskLater(Game.plugin,20*60*5)
    }
}