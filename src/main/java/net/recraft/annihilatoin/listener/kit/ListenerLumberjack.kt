package net.recraft.annihilatoin.listener.kit

import net.recraft.annihilatoin.listener.SuitableTool
import net.recraft.annihilatoin.objects.Game.kitType
import net.recraft.annihilatoin.objects.Game.team
import net.recraft.annihilatoin.objects.kit.KitType
import net.recraft.annihilatoin.objects.kit.Lumberjack
import net.recraft.annihilatoin.util.Util
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent

class ListenerLumberjack: Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    fun onDamage(event: EntityDamageByEntityEvent) {
        val victim = if (event.entity is Player) {event.entity as Player} else return
        val attacker = if (event.damager is Player) {event.damager as Player} else return
        if (victim.team() == attacker.team()) return
        if (attacker.kitType() != KitType.LUMBERJACK) return
        val item = attacker.itemInHand
        val tool = SuitableTool.Tool.getTool(item)
        if (tool != SuitableTool.Tool.AXE) return
        victim.inventory.armorContents.forEach {
            val currentDurability = Util.getItemDurability(it)
            Util.setItemDurability(it, currentDurability-Lumberjack.durabilityDamage)
        }
        victim.updateInventory()
    }
}