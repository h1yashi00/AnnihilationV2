package net.recraft.annihilatoin.listener

import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.objects.Game.kitType
import net.recraft.annihilatoin.objects.Game.team
import net.recraft.annihilatoin.objects.PlayerLeaveUnfairAdvantage
import net.recraft.annihilatoin.objects.kit.KitGenerator
import net.recraft.annihilatoin.realTeleport
import net.recraft.annihilatoin.scoreboard.ScoreboardAnni
import net.recraft.annihilatoin.util.ScoreboardUtil
import org.bukkit.Bukkit
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
        event.player.scoreboard = Bukkit.getScoreboardManager().newScoreboard
        val scoreboard: ScoreboardUtil = ScoreboardAnni
        scoreboard.display(event.player)
        val player = event.player
        val uuid = player.uniqueId
        player.inventory.apply {
            setHelmet       (ItemStack(Material.AIR))
            setChestplate   (ItemStack(Material.AIR))
            setLeggings     (ItemStack(Material.AIR))
            setBoots        (ItemStack(Material.AIR))
        }
        player.inventory.clear()
        if (!Game.isStart) player.realTeleport(Game.lobby.spawnLocation)
        val kitType = player.kitType()
        val kit = KitGenerator.get(kitType)
        kit.setInit(player)
        val team = player.team()
        if (team == null) {player.realTeleport(Game.lobby.spawnLocation)}
        else playerLeaveUnfairAdvantage.respawn(player, team)
    }
}