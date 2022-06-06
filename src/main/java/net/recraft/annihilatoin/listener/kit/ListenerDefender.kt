package net.recraft.annihilatoin.listener.kit

import net.recraft.annihilatoin.listener.CoolDown
import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.objects.Game.kitType
import net.recraft.annihilatoin.objects.Game.team
import net.recraft.annihilatoin.objects.GameTeam
import net.recraft.annihilatoin.objects.kit.Defender
import net.recraft.annihilatoin.objects.kit.KitType
import net.recraft.annihilatoin.realTeleport
import net.recraft.annihilatoin.util.Util
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable

class ListenerDefender: Listener {
    private val coolDown = CoolDown(
        Defender.coolDownTime,
    ) {}
    init {
        object: BukkitRunnable() {
            override fun run() {
                Bukkit.getOnlinePlayers().forEach {
                    if (it.kitType() != KitType.DEFENDER) return@forEach
                    val team = it.team() ?: return@forEach
                    if (!isAbilityActiveDistance(team.objects.nexus.location, it.location)) return@forEach
                    val updateHealth = 20 + getAbilityHealth(team)
                    it.healthScale = updateHealth.toDouble()
                    it.addPotionEffect(PotionEffect(PotionEffectType.REGENERATION, 20 * 10, 1))
                }
            }
        }.runTaskTimer(Game.plugin, 0,20 * 5)
    }
    private fun getAbilityHealth(team: GameTeam): Int {
        val hp = team.objects.nexus.hp
        return when {
            hp > 68 -> 0
            hp > 59 -> 1
            hp > 53 -> 2
            hp > 44 -> 3
            hp > 34 -> 4
            hp > 25 -> 5
            hp > 13 -> 6
            hp > 4  -> 7
            else -> 8
        }
    }
    private fun isAbilityActiveDistance(from: Location, to: Location): Boolean {
        return Util.euclideanDistance(from, to) < Defender.maxDistance
    }
    @EventHandler
    fun onRightClick(event: PlayerInteractEvent) {
        val player = event.player
        if (player.kitType() != KitType.DEFENDER) return
        val item = player.itemInHand
        if (!Defender.isItem(item)) return
        val team = player.team() ?: return
        if (!coolDown.isReady(player)) {
            coolDown.isNotReadyMsg(player)
            return
        }
        coolDown.add(player)
        player.realTeleport(team.objects.defender)
        player.sendMessage("${ChatColor.GOLD}自軍ネクサスまで移動しました｡")
    }
}