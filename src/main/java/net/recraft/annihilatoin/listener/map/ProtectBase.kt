package net.recraft.annihilatoin.listener.map

import net.recraft.annihilatoin.listener.PlayerPlaceBlock
import net.recraft.annihilatoin.objects.GameTeam
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent

class ProtectBase: Listener {
    @EventHandler
    fun onPlaceBlock(event: BlockPlaceEvent) {
        val block = event.block
        val player = event.player
        if (player.gameMode == GameMode.CREATIVE) return
        GameTeam.values().forEach {
            if (it.objects.base.contains(block.location)) {
                event.isCancelled = true
                player.sendMessage("${ChatColor.RED}This area is protected")
            }
        }
    }
    @EventHandler
    fun onBreak(event: BlockBreakEvent) {
        val block = event.block
        val player = event.player
        if (player.gameMode == GameMode.CREATIVE) return
        GameTeam.values().forEach {
            if (it.objects.base.contains(block.location)) {
                event.isCancelled = true
                player.sendMessage("${ChatColor.RED}This area is protected")
            }
        }
    }

}