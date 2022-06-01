package net.recraft.annihilatoin.listener

import net.recraft.annihilatoin.objects.Game
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import java.util.*
import kotlin.collections.HashMap

class CoolDown(
    private val coolDown: Int,
    private val readyMsg: String = "使用可能になりました｡",
    private val notReadyMsg: String = "スキル使用できるまであと",
    private val readyAction: (Player) -> Unit,
) {
    init {
        object: BukkitRunnable() {
            override fun run() {
                if (coolDownPlayers.isEmpty()) return
                val it = coolDownPlayers.keys.iterator()
                while (it.hasNext()) {
                    val uuid = it.next()
                    val coolDown = coolDownPlayers[uuid]!!
                    if (coolDown <= 0) {
                        if (readyMsg != "") {
                            Bukkit.getPlayer(uuid).sendMessage("${ChatColor.GRAY}$readyMsg")
                        }
                        coolDownPlayers.remove(uuid)
                        val p = Bukkit.getPlayer(uuid) ?: continue
                        readyAction(p)
                        continue
                    }
                    coolDownPlayers[uuid] = coolDown - 1
                }
            }
        }.runTaskTimer(Game.plugin, 0, 20)
    }
    private val coolDownPlayers = HashMap<UUID, Int>()

    fun add(player: Player) {
        coolDownPlayers[player.uniqueId] = coolDown
    }

    fun isNotReadyMsg(player: Player) {
        if (notReadyMsg != "") {
            player.sendMessage("${ChatColor.RED}$notReadyMsg${coolDownPlayers[player.uniqueId]}")
        }
    }

    fun isReady(player: Player): Boolean {
        return !coolDownPlayers.containsKey(player.uniqueId)
    }
}