package net.recraft.annihilatoin.listener

import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.inventory.ItemStack

class Soulbound : Listener {
    @EventHandler
    fun onPlayerDropItem(event: PlayerDropItemEvent) {
        val item = event.itemDrop.itemStack
        if (!isSoulbound(item)) return
        val player = event.player
        player.playSound(player.location, Sound.BLAZE_HIT, 0.5f, 0.1f)
        event.itemDrop.remove()
    }

    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        val it = event.drops.iterator()
        while (it.hasNext()) {
            if (isSoulbound(it.next())) {
                it.remove()
            }
        }
    }

    companion object {
        fun isSoulbound(item: ItemStack): Boolean {
            val meta = item.itemMeta
            val lores = meta.lore ?: return false
            for (lore in lores) {
                if (lore.equals(data)) {
                    return true
                }
            }
            return false
        }

        private val data: String = ChatColor.YELLOW.toString() + "Soulbound"
        fun set(item: ItemStack) {
            val meta = item.itemMeta
            val lores = meta.lore
            if (lores == null) {
                val lore: MutableList<String> = ArrayList()
                lore.add(data)
                meta.lore = lore
            } else {
                lores.add(data)
                meta.lore = lores
            }
            item.itemMeta = meta
        }
    }
}
