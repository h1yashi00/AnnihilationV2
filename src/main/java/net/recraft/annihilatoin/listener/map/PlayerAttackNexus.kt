package net.recraft.annihilatoin.listener.map

import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.objects.Game.team
import net.recraft.annihilatoin.objects.GameTeam
import net.recraft.annihilatoin.objects.menu.ShopWeaponMenu
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.scheduler.BukkitRunnable


class PlayerAttackNexus: Listener {
    private fun delayRespawn(brokenNexus: Block) {
        brokenNexus.type = Material.BARRIER
        object: BukkitRunnable() {
            override fun run() {
                brokenNexus.location.block.type = Material.ENDER_STONE
            }
        }.runTaskLater(Game.plugin, 3)
    }
    @EventHandler
    fun onBreakBlock(event: BlockBreakEvent){
        val brokenBlock = event.block
        if (brokenBlock.type != Material.ENDER_STONE) return
        if (!Game.isStart) return
        val damagedNexus = GameTeam.getNexus(brokenBlock.location) ?: return
        event.isCancelled = true
        val player = event.player
        val playerY = player.location.blockY +1
        val blockY = brokenBlock.y
        // ネクサスの位置がプレイヤーの頭の位置から+-1の範囲でしか受け付けない
        if ( !(playerY == blockY || playerY == blockY +1 || playerY == blockY -1) ) {
            player.sendMessage("${ChatColor.RED}プレイヤーの位置が高すぎるか低すぎます｡ civBreak?")
            return
        }
        if (ShopWeaponMenu.isSpecialTool(player.itemInHand)) {
            player.sendMessage("${ChatColor.RED}ショップアイテムでは攻撃できません")
            return
        }
        if (Game.phase.currentPhase <= 2) {
            player.sendMessage("${ChatColor.RED}フェイズ3移行から攻撃できるようになります｡")
            return
        }
        val playerTeam = player.team() ?: return
        if (playerTeam.objects.nexus == damagedNexus) return
        damagedNexus.damage(player)
        if (damagedNexus.isAlive()) { delayRespawn(brokenBlock); return;}
        damagedNexus.destroyedEvent()
        if (!GameTeam.isFinishStatus()) return
        Game.end()
    }
}