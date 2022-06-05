package net.recraft.annihilatoin.listener.kit

import net.recraft.annihilatoin.listener.CoolDown
import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.objects.Game.kitType
import net.recraft.annihilatoin.objects.kit.KitType
import net.recraft.annihilatoin.objects.kit.Miner
import net.recraft.annihilatoin.util.ParticleEffect
import org.bukkit.block.Block
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class ListenerMiner: Listener {
    private val coolDown = CoolDown(Miner.coolDown) {
    }
    private val abilityEffectPlayers = CoolDown(Miner.abilityEffectTime, readyMsg = ""){}

    @EventHandler
    fun onLeftClickBlock(event: PlayerInteractEvent) {
        if (event.action != Action.LEFT_CLICK_BLOCK) return
        val block = event.clickedBlock
        val player = event.player
        if (!abilityEffectPlayers.contains(player)) return
        object: BukkitRunnable() {
            var i = 2
            override fun run() {
                if (i < 0) {
                    cancel()
                }
                displayParticle(block)
                i -= 1
            }
        }.runTaskTimer(Game.plugin, 0,5)
    }
    private fun displayParticle(block: Block){
        val loc1 = block.location.clone().apply{x+=0.5; y+=0.5}
        val loc2 = block.location.clone().apply{x+=0.5; y+=0.5; z+=1}
        val loc3 = block.location.clone().apply{x+=1; y+=0.5; z+=0.5}
        val loc4 = block.location.clone().apply{x-=0; y+=0.5; z+=0.5}
        ParticleEffect.REDSTONE.display(255F, 185F, 33F, 0.004.toFloat(), 0, loc1, 10.0)
        ParticleEffect.REDSTONE.display(255F, 185F, 33F, 0.004.toFloat(), 0, loc2, 10.0)
        ParticleEffect.REDSTONE.display(255F, 185F, 33F, 0.004.toFloat(), 0, loc3, 10.0)
        ParticleEffect.REDSTONE.display(255F, 185F, 33F, 0.004.toFloat(), 0, loc4, 10.0)
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onBreakBlock(event: BlockBreakEvent) {
        val player = event.player ?: return
        if (!abilityEffectPlayers.contains(player)) return
        val block = event.block
        if (!Miner.isAffectedOre(block.type)) return
        val item = ItemStack(block.type)
        player.inventory.addItem(item)
    }

    @EventHandler
    fun onRightClickGoldRush(event: PlayerInteractEvent) {
        if (!(event.action == Action.RIGHT_CLICK_AIR || event.action == Action.RIGHT_CLICK_BLOCK)) return
        val player = event.player
        if (!coolDown.isReady(player)) {
            coolDown.isNotReadyMsg(player)
            return
        }
        if (player.kitType() != KitType.MINER) return
        val item = player.itemInHand
        if (!Miner.isItem(item)) return
        coolDown.add(player)
        abilityEffectPlayers.add(player)
    }
}