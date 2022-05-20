package net.recraft.annihilatoin.listener.map

import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.objects.ResourceBlocks
import net.recraft.annihilatoin.objects.kit.KitType
import net.recraft.annihilatoin.objects.kit.Miner
import net.recraft.annihilatoin.objects.menu.ShopWeaponMenu
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
        val resourceBlock = ResourceBlocks.getStatus(brokenBlock) ?: return
        val savedBlockData = brokenBlock.data
        event.isCancelled = true
        if (ShopWeaponMenu.isSpecifalTool(player.itemInHand)) {
            player.sendMessage("Special Tool cant break ResourceBlocks!!!")
            return
        }
        if (brokenBlock.type == Material.DIAMOND_ORE) {
            if (Game.phase.currentPhase <= 2) {
                player.sendMessage("current Phase is ${Game.phase.currentPhase}!! you cannot break Diamond")
                return
            }
        }
        brokenBlock.type = if (resourceBlock.setCobble) { Material.COBBLESTONE } else {Material.AIR}
        val kit = Game.getPlayerData(player.uniqueId).kitType
        if (kit == KitType.MINER) {
            if (Miner.isAffectedOre(resourceBlock.type)) {
                val item = resourceBlock.getItemStack()
                item.amount = 1
                player.inventory.addItem(item)
            }
        } else if (kit == KitType.LUMBERJACK) {
            if (Util.isLog(resourceBlock.type)) {
                val item = resourceBlock.getItemStack()
                item.amount = 1
                player.inventory.addItem(item)
            }
        }
        player.inventory.addItem(resourceBlock.getItemStack())
        val exp = if (kit == KitType.ENCHANTER) { resourceBlock.exp * 2 }  else { resourceBlock.exp }
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