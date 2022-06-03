package net.recraft.annihilatoin.listener.special_item

import net.recraft.annihilatoin.listener.CoolDown
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerInteractEvent

class ListenerLanchPad: Listener {
    val coolDown = CoolDown(10,"",""){}
    @EventHandler
    fun onPlayerFallDamage(event: EntityDamageEvent) {
        if (event.cause != EntityDamageEvent.DamageCause.FALL) return
        val player = if (event.entity is Player) { event.entity as Player } else return
        if (coolDown.isReady(player)) return
        event.isCancelled = true
    }
    @EventHandler
    fun onPressurePlate(event: PlayerInteractEvent) {
        if (event.action != Action.PHYSICAL) return
        val block = event.clickedBlock.location.block
        if (!isLanchPad(block)) return
        // activate
        val player = event.player
        coolDown.add(player)
        val dir = player.location.direction.clone().multiply(4).apply{setY(0)}
        player.velocity = dir
        player.playSound(player.location, Sound.WITHER_SHOOT, 1F,3F)
    }

    private fun isLanchPad(block: Block): Boolean {
        val pushedBlock = block.location.clone().block
        if (pushedBlock.type != Material.STONE_PLATE) return false
        val pushedBlockUnder  = block.location.clone().apply{y-=1}.block
        if (pushedBlockUnder.type == Material.IRON_BLOCK) return true
        if (pushedBlockUnder.type == Material.REDSTONE_LAMP_OFF) return true
        if (pushedBlockUnder.type == Material.REDSTONE_LAMP_ON) return true
        if (pushedBlockUnder.type == Material.GOLD_BLOCK) return true
        return false
    }
}