package net.recraft.annihilatoin.listener

import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.objects.Game.kitType
import net.recraft.annihilatoin.objects.Game.team
import net.recraft.annihilatoin.util.Util
import org.bukkit.ChatColor
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable
import java.lang.StringBuilder

class PlayerDeath: Listener {
    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        object:BukkitRunnable() {
            override fun run() {
                event.entity.spigot().respawn()
            }
        }.runTaskLater(Game.plugin, 2)
        val victim = if (event.entity is Player) { event.entity as Player } else return
        val killer = if (victim.killer is Player) { victim.killer as Player } else return
        event.deathMessage =
            "${Util.getColoredPlayersName(victim, victim.team() ?: return)}${victim.team()!!.chatColor}(${victim.kitType().shortName})${ChatColor.GRAY}は" +
            "${Util.getColoredPlayersName(killer, killer.team() ?: return)}${killer.team()!!.chatColor}(${killer.kitType().shortName})${ChatColor.GRAY}に倒された｡" +
            "${sword(killer)}${getEffectColored(killer)}"

    }
    private fun getItemColored(item: ItemStack): String {
        return when {
            item.type.name.contains("diamond", true) -> "${ChatColor.DARK_AQUA}■${ChatColor.GRAY}"
            item.type.name.contains("iron", true)    -> "${ChatColor.WHITE}■${ChatColor.GRAY}"
            else -> ""
        }
    }
    private fun getEffectColored(player: Player): String {
        val msg = StringBuilder()
        player.activePotionEffects.forEach { if (it.type == PotionEffectType.REGENERATION) if (it.amplifier == 4) msg.append("${ChatColor.GOLD}[G]") }
        player.activePotionEffects.forEach {
            when(it.type) {
                PotionEffectType.INVISIBILITY    -> msg.append("${ChatColor.GRAY}■")
                PotionEffectType.INCREASE_DAMAGE -> msg.append("${ChatColor.DARK_RED}■")
                PotionEffectType.FIRE_RESISTANCE -> msg.append("${ChatColor.GOLD}■")
                PotionEffectType.REGENERATION    -> { msg.append("${ChatColor.LIGHT_PURPLE}■") }
                PotionEffectType.SPEED           -> msg.append("${ChatColor.AQUA}■")
                PotionEffectType.JUMP            -> msg.append("${ChatColor.DARK_GREEN}■")
            }
        }
        return msg.toString()
    }
    private fun sword(player: Player): String {
        val item = player.itemInHand
        val msg = StringBuilder()
        msg.append(getItemColored(item))
        item.enchantments.forEach {
            if (it.key == Enchantment.DAMAGE_ALL) {
                msg.append("[${it.value}]")
            }
        }
        return msg.toString()
    }
}