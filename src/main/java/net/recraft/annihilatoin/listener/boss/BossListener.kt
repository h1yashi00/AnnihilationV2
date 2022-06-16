package net.recraft.annihilatoin.listener.boss

import net.recraft.annihilatoin.event.BossSpawnEvent
import net.recraft.annihilatoin.isSame
import net.recraft.annihilatoin.objects.Game.team
import net.recraft.annihilatoin.objects.builder.ItemStackBuilder
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Entity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerPickupItemEvent
import org.bukkit.inventory.ItemStack
import java.util.*

class BossListener: Listener {
    var entity: Entity? = null
    var bossBuff: ItemStack? = null

    @EventHandler
    fun bossSpawn(event: BossSpawnEvent) {
        entity = event.entity
    }

    @EventHandler
    fun bossDeath(event: EntityDeathEvent) {
        if (entity == null) return
        if (event.entity != entity) return
        event.droppedExp = 0
        event.drops.clear()
        entity = null
        bossBuff = ItemStackBuilder(Material.NETHER_STAR).title("${ChatColor.GOLD}${UUID.randomUUID()}").build()
        event.drops.add(bossBuff)
    }

    @EventHandler
    fun playerGetDroppedBossBuff(event: PlayerPickupItemEvent) {
        val item = event.item.itemStack
        if (!item.isSame(bossBuff)) return
        val player = event.player
        event.isCancelled = true
        event.item.remove()
        val team = player.team()
        Bukkit.getOnlinePlayers().forEach {
            if (it.team() == team) {
                it.inventory.addItem(BossBuffListener.itemBossBuff)
            }
        }
    }
}