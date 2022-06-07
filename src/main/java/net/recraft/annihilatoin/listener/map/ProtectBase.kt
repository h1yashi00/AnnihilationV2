package net.recraft.annihilatoin.listener.map

import net.recraft.annihilatoin.objects.GameTeam
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent

class ProtectBase: Listener {
    @EventHandler
    fun onBreak(event: BlockBreakEvent) {
        val block = event.block
        val player = event.player
        GameTeam.values().forEach {
            if (it.objects.base.contains(block.location)) {
                event.isCancelled = true
                player.sendMessage("${ChatColor.RED}This area is protected")
            }
        }
    }

}