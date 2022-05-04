package net.recraft.annihilatoin.listener

import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.objects.kit.Kit
import net.recraft.annihilatoin.objects.kit.Miner
import net.recraft.annihilatoin.util.Util
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.scheduler.BukkitRunnable

class PlayerBreakResourceBlock : Listener {
    @EventHandler
    fun onBreakResource(event: BlockBreakEvent) {
        val player = event.player
        val brokenBlock = event.block
        // ResourceBlockじゃなければ返す
        val resourceBlock = Util.getStatus(brokenBlock) ?: return
        val savedBlockData = brokenBlock.data
        event.isCancelled = true
        brokenBlock.type = if (resourceBlock.setCobble) { Material.COBBLESTONE } else {Material.AIR}
        val kit = Game.getKit(player.uniqueId)
        if (kit == Kit.MINER) {
            if (Miner.isAffectedOre(resourceBlock.type)) {
                val item = resourceBlock.getItemStack()
                item.amount = 1
                player.inventory.addItem(item)
            }
        } else if (kit == Kit.LUMBERJACK) {
            if (Util.isLog(resourceBlock.type)) {
                val item = resourceBlock.getItemStack()
                item.amount = 1
                player.inventory.addItem(item)
            }
        }
        player.inventory.addItem(resourceBlock.getItemStack())
        val exp = if (kit == Kit.ENCHANTER) { resourceBlock.exp * 2 }  else { resourceBlock.exp }
        player.giveExp(exp)
        val scheduler = Bukkit.getScheduler();
        val runnable = object : BukkitRunnable() {
            override fun run() {
                brokenBlock.type = resourceBlock.type;
                brokenBlock.data = savedBlockData
            }
        }
        scheduler.runTaskLater(Game.plugin, runnable, resourceBlock.restore.toLong());
    }
}