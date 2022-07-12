package net.recraft.annihilatoin.listener.map

import net.recraft.annihilatoin.database.AnnihilationStatsColumn
import net.recraft.annihilatoin.database.Database
import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.objects.Game.kitType
import net.recraft.annihilatoin.objects.ResourceBlocks
import net.recraft.annihilatoin.objects.kit.KitType
import net.recraft.annihilatoin.objects.kit.Miner
import net.recraft.annihilatoin.objects.menu.ShopWeaponMenu
import net.recraft.annihilatoin.util.Util
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

class PlayerBreakResourceBlock : Listener {
    @EventHandler(priority = EventPriority.HIGHEST)
    fun onBreakResource(event: BlockBreakEvent) {
        val player = event.player


        val brokenBlock = event.block
        // ResourceBlockじゃなければ返す
        val resourceBlock = ResourceBlocks.getStatus(brokenBlock) ?: return
        val savedBlockData = brokenBlock.data
        event.isCancelled = true
        if (ShopWeaponMenu.isSpecialTool(player.itemInHand)) {
            player.sendMessage("${ChatColor.RED}ショップアイテムは資源ブロックを採掘できません")
            return
        }
        if (brokenBlock.type == Material.DIAMOND_ORE) {
            if (Game.phase.currentPhase <= 1) {
                player.sendMessage("${ChatColor.RED}フェイズ2移行からダイヤモンドは採掘できます")
                return
            }
        }
        brokenBlock.type = if (resourceBlock.setCobble) { Material.COBBLESTONE } else {Material.AIR}
        val kit = player.kitType()
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
        // 幸運エンチャント
        var fortuneAmount = resourceBlock.getItemStack().amount
        if (isAffectedFortune(resourceBlock.type)) {
            fortuneAmount = isFortune(player.itemInHand)
        }
        player.inventory.addItem(resourceBlock.getItemStack().clone().apply { amount = fortuneAmount })
        val exp = if (kit == KitType.ENCHANTER) { resourceBlock.exp * 2 }  else { resourceBlock.exp }

        // statics
        object: BukkitRunnable() {
            override fun run() {
                Database.incCount(AnnihilationStatsColumn.ORES_MINED, player.uniqueId)
                Database.addCount(AnnihilationStatsColumn.EXPS_GAINED, player.uniqueId, exp)
            }
        }.runTaskAsynchronously(Game.plugin)

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

    private fun isAffectedFortune(type: Material): Boolean {
        return when(type) {
            Material.DIAMOND_ORE -> true
            Material.EMERALD_ORE -> true
            Material.COAL_ORE    -> true
            else -> false
        }
    }

    private fun isFortune(item: ItemStack): Int {
        item.enchantments.forEach {
            if (it.key == Enchantment.LOOT_BONUS_BLOCKS) return it.value + 1
        }
        return 1
    }
}