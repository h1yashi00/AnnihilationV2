package net.recraft.annihilatoin.listener.kit

import net.recraft.annihilatoin.hide
import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.objects.kit.KitType
import net.recraft.annihilatoin.show
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class ListenerSpy: Listener {
    private val invisiblePlayers = mutableSetOf<UUID>()
    @EventHandler
    fun onJoin(event: PlayerJoinEvent) {
        val player = event.player
        invisiblePlayers.forEach { val invPlayer = Bukkit.getPlayer(it); player.hidePlayer(invPlayer) }
    }
    @EventHandler
    fun onSneak(event: PlayerToggleSneakEvent) {
        val player = event.player
        if (Game.getPlayerData(player.uniqueId).kitType != KitType.SPY) return
        val delayTask = object: BukkitRunnable() {
            var i = 0
            override fun run() {
                if (i == 20*3) {
                    player.hide()
                    invisiblePlayers.add(player.uniqueId)
                    player.sendMessage("${ChatColor.GRAY}You are currently invisible!")
                    cancel()
                }
                if (!player.isSneaking) {
                    player.sendMessage("${ChatColor.RED}invisible task was canceled!!!!")
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
            invisiblePlayers.remove(player.uniqueId)
            player.show()
        }
    }
}