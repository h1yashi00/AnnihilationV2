package net.recraft.annihilatoin.listener.special_item

import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.objects.GameTeam
import net.recraft.annihilatoin.objects.SpecialItem
import net.recraft.annihilatoin.realTeleport
import net.recraft.annihilatoin.util.ParticleEffect
import org.bukkit.Effect
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.block.Block
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class ListenerTransPortItem: Listener {
    private data class SavedBlock(val type: Material, val data: Byte)
    private data class TransPort(var loc1: Block) {
        private val savedBlock1: SavedBlock = SavedBlock(loc1.type, loc1.data)
        private var savedBlock2: SavedBlock? = null
        var loc2: Block? = null
            set(value) {
                if (value == null) return
                savedBlock2 = SavedBlock(value.type, value.data)
                value.type = Material.QUARTZ_ORE
                field = value
            }
        init {
            loc1.type = Material.QUARTZ_ORE
        }
        fun remove() {
            loc1.world.playEffect(loc1.location, Effect.STEP_SOUND, Material.QUARTZ_ORE)
            loc1.world.playEffect(loc2!!.location, Effect.STEP_SOUND, Material.QUARTZ_ORE)
            loc1.type = savedBlock1.type
            loc1.data = savedBlock1.data
            loc2!!.type = savedBlock2!!.type
            loc2!!.data= savedBlock2!!.data
        }
    }
    init {
        object: BukkitRunnable() {
            override fun run() {
                tps.values.forEach {
                    if (it.loc2 == null) return@forEach
                    showEffect(it.loc1.location);
                    showEffect(it.loc2!!.location) }
            }
        }.runTaskTimerAsynchronously(Game.plugin, 0, 5)
    }
    private val tps = HashMap<UUID, TransPort>()
    // プレイヤー一人一つ???
    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_BLOCK) return
        val player = event.player
        val item = player.itemInHand ?: return
        if (!SpecialItem.isTeleportItem(item)) return
        if (!isPlaceAble(event.clickedBlock.location)) return
        val block = event.clickedBlock
        // TPの片方を設置済み
        if (!tps.contains(player.uniqueId)) {
            tps[player.uniqueId] = TransPort(block)
        }
        else {
            val tp = tps[player.uniqueId]!!
            if (tp.loc2 != null) {
                tp.remove()
                tps.remove(player.uniqueId)
                return
            }
            tp.loc2 = block
        }
    }
    private fun isPlaceAble(loc: Location): Boolean {
        GameTeam.values().forEach {
            if (it.objects.nexus.location == loc) return false
            if (it.objects.enderFurnace.location == loc) return false
            if (it.objects.enderChest.location == loc) return false
            if (it.objects.shopBrewing.location == loc) return false
            if (it.objects.shopWeapon.location == loc) return false
            if (it.objects.spawn1.location == loc) return false
            if (it.objects.spawn2.location == loc) return false
            if (it.objects.spawn3.location == loc) return false
        }
        if (isTransPortLocation(loc)) return false
        return true
    }
    private fun isTransPortLocation(loc: Location): Boolean {
        tps.values.forEach {transPort ->  if (transPort.loc1.location == loc || transPort.loc2?.location == loc) return true}
        return false
    }
    private fun showEffect(location: Location) {
        val addX = Random().nextDouble()
        val addY = Random().nextDouble() + 1
        val addZ = Random().nextDouble()
        val spawn = location.apply {x+=addX; y+=addY; z+=addZ}
        ParticleEffect.CLOUD.display(0F, 0F, 0F, 0F, 2, spawn, 256.0)
    }
    @EventHandler
    fun onSneak(event: PlayerToggleSneakEvent) {
        if (!event.isSneaking) return
        val player = event.player
        val block = player.location.block.location.apply {y-=1}.block
        tps.values.forEach {
            if (it.loc1 == block) {
                player.realTeleport(it.loc2!!.location!!.apply { y += 1 }); player.playSound(player.location,
                    Sound.ENDERMAN_TELEPORT,
                    1f,
                    1f)
            } else if (it.loc2 == block) {
                player.realTeleport(it.loc1.location!!.apply { y += 1 }); player.playSound(player.location,
                    Sound.ENDERMAN_TELEPORT,
                    1f,
                    1f)
            }
        }
    }
    @EventHandler
    fun onBreak(event: BlockBreakEvent) {
        if (event.block.type != Material.QUARTZ_ORE) return
        if (!isTransPortLocation(event.block.location)) return
        event.isCancelled = true
    }
}