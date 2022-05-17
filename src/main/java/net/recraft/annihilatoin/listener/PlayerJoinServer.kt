package net.recraft.annihilatoin.listener

import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.objects.PlayerLeaveUnfairAdvantage
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.ItemStack

class PlayerJoinServer(
    private val playerLeaveUnfairAdvantage: PlayerLeaveUnfairAdvantage,
): Listener {
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        event.player.scoreboard = Game.scoreboard
        val player = event.player
        val uuid = player.uniqueId
        player.inventory.apply {
            setHelmet       (ItemStack(Material.AIR))
            setChestplate   (ItemStack(Material.AIR))
            setLeggings     (ItemStack(Material.AIR))
            setBoots        (ItemStack(Material.AIR))
        }
        player.inventory.clear()
        if (!Game.isStart) player.teleport(Game.lobby.spawnLocation)
        val team = Game.getPlayerData(uuid).team
        if (team == null) {player.teleport(Game.lobby.spawnLocation)}
        else playerLeaveUnfairAdvantage.respawn(player, team)
    }
}