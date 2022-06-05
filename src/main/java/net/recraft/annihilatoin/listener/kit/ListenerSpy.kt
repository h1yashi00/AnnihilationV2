package net.recraft.annihilatoin.listener.kit

import net.recraft.annihilatoin.hide
import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.objects.Game.kitType
import net.recraft.annihilatoin.objects.kit.KitType
import net.recraft.annihilatoin.show
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.block.Block
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.*
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class ListenerSpy: Listener {
    private data class InvSavedData(val block: Block) {}
    private class InvPlayers {
        private val invisiblePlayers = HashMap<UUID, InvSavedData>()
        fun add(player: Player) {
            invisiblePlayers[player.uniqueId] = InvSavedData(player.location.block)
            player.hide()
            player.sendMessage("${ChatColor.GRAY}You are currently invisible!")
        }
        fun remove(player: Player) {
            invisiblePlayers.remove(player.uniqueId)
            player.show()
            player.sendMessage("${ChatColor.RED}invisible task was canceled!!!!")
        }
        fun contains(player: Player): Boolean {
            return invisiblePlayers.contains(player.uniqueId)
        }
        fun keys(): MutableSet<UUID> {
            return invisiblePlayers.keys
        }

        fun isMoved(player: Player?): Boolean {
            if (player == null) return false
            val data = invisiblePlayers[player.uniqueId] ?: return false
            if (data.block == player.location.block) return false
            return true
        }
    }
    private val invPlayersData = InvPlayers()
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
        invPlayersData.keys().forEach { val invPlayer = Bukkit.getPlayer(it); player.hidePlayer(invPlayer) }
    }
    @EventHandler
    fun onQuit(event: PlayerQuitEvent) {
        val player = event.player
        invPlayersData.remove(player)
    }
    @EventHandler
    fun onInvPlayerInteract(event: PlayerInteractEvent) {
        val player = event.player
        if (player.kitType() != KitType.SPY) return
        if (!invPlayersData.contains(player)) return
        invPlayersData.remove(player)
    }

    @EventHandler
    fun onMove(event: PlayerMoveEvent) {
        val player = event.player
        if (player.kitType() != KitType.SPY) return
        if (!invPlayersData.contains(player)) return
        if (!invPlayersData.isMoved(player)) return
        invPlayersData.remove(player)
    }
    // ここにスレッドを使うべきか?
    @EventHandler
    fun onEnemyInteract(event: PlayerInteractEvent) {
        if (!(event.action == Action.LEFT_CLICK_AIR || event.action == Action.LEFT_CLICK_BLOCK || event.action == Action.RIGHT_CLICK_BLOCK)) return
        val player = event.player
        val hitPlayer = threeBlockRangePlayer(player)?: return
        if (!invPlayersData.contains(hitPlayer)) return
        hitPlayer.sendMessage("You found by ${player.name}")
        invPlayersData.remove(hitPlayer)
    }

    private fun threeBlockRangePlayer(player: Player): Player? {
        val loc = player.location.clone()
        val dir = loc.direction.clone()
        for (i in 0..3) {
            dir.multiply(1)
            val newLoc = loc.add(dir)
            return getPlayerByLocation(player, newLoc, 1.5F) ?: continue
        }
        return null
    }

    private fun getPlayerByLocation(excludePlayer: Player, loc: Location, r: Float): Player? {
        loc.world.entities.forEach {
            if (it.location.distanceSquared(loc) < r) {
                if (it is Player) {
                    if (it.player.uniqueId == excludePlayer.uniqueId) {
                        return@forEach
                    }
                    return it.player
                }
            }
        }
        return null
    }

    @EventHandler
    fun onSneak(event: PlayerToggleSneakEvent) {
        val player = event.player
        if (player.kitType() != KitType.SPY) return
        val delayTask = object: BukkitRunnable() {
            var i = 0
            override fun run() {
                if (i == 20*3) {
                    invPlayersData.add(player)
                    cancel()
                }
                if (!player.isSneaking) {
                    cancel()
                }
                i += 1
            }
        }
        // 不思議に思うかもしれないが､sneakを押した瞬間はtrueではなく､falseになる｡
        // eventが発生する前に段階だから｡
        if (!player.isSneaking) {
            delayTask.runTaskTimer(Game.plugin, 0, 1)
        }
        else {
            invPlayersData.remove(player)
        }
    }
}