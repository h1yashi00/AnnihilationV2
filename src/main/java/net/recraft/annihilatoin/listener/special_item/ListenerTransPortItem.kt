package net.recraft.annihilatoin.listener.special_item

import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.objects.GameTeam
import net.recraft.annihilatoin.objects.ResourceBlocks
import net.recraft.annihilatoin.objects.SpecialItem
import net.recraft.annihilatoin.realTeleport
import net.recraft.annihilatoin.team
import net.recraft.annihilatoin.util.ParticleEffect
import net.recraft.annihilatoin.util.Util
import org.bukkit.*
import org.bukkit.block.Block
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class ListenerTransPortItem: Listener {
    private data class SavedBlock(val type: Material, val data: Byte)
    private data class TransPort(val uuid: UUID, var loc1: Block) {
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
            loc1.type = savedBlock1.type
            loc1.data = savedBlock1.data
            if (loc2 == null) return
            loc2!!.world.playEffect(loc2!!.location, Effect.STEP_SOUND, Material.QUARTZ_ORE)
            loc2!!.type = savedBlock2!!.type
            loc2!!.data= savedBlock2!!.data
        }
    }
    private data class CoolDown(val uuid: UUID, var coolDown: Int = SpecialItem.handicapCapCoolDown) {
        fun pass() {
            coolDown -= 1
        }
    }
    private val handicapCapCoolDown = object : ArrayList<CoolDown>() {
        fun contains(uuid: UUID): Boolean {
            this.forEach { if (it.uuid == uuid) return true }
            return false
        }
        operator fun get(uuid: UUID): CoolDown? {
            this.forEach { if (it.uuid == uuid) return it}
            return null
        }
        fun remove(uuid: UUID) {
            this.forEach {
                if (it.uuid == uuid) { this.remove(it); return }
            }
        }
    }
    init {
        object: BukkitRunnable() {
            override fun run() {
                tps.values().forEach {
                    if (it.loc2 == null) return@forEach
                    showEffect(it.loc1.location);
                    showEffect(it.loc2!!.location) }
            }
        }.runTaskTimerAsynchronously(Game.plugin, 0, 5)
        
        object: BukkitRunnable() {
            override fun run() {
                val ite = handicapCapCoolDown.iterator()
                while(ite.hasNext()) {
                    val it = ite.next()
                    if (it.coolDown <= 0) {
                        ite.remove()
                        continue
                    }
                    it.pass()
                }
            }
        }.runTaskTimerAsynchronously(Game.plugin, 0, 20)
    }
    private val tps = object {
        val buffer = ArrayList<TransPort>()
        fun remove(uuid: UUID) {
            buffer.iterator().forEach { if (it.uuid == uuid) it.remove(); buffer.remove(it) }
        }
        fun remove(tp: TransPort) {
            tp.remove()
            buffer.remove(tp)
        }
        fun add(tp: TransPort) {
            buffer.add(tp)
        }
        operator fun get(uuid: UUID): TransPort? {
            buffer.forEach { if (it.uuid == uuid) return it }
            return null
        }
        fun clear() {
            buffer.iterator().forEach {
                it.remove()
            }
            buffer.clear()
        }
        fun values(): MutableIterator<TransPort> {
            return buffer.iterator()
        }
        fun contains(uuid: UUID): Boolean {
            buffer.forEach { if(it.uuid == uuid) return true }
            return false
        }
    }
    fun disable() {
        tps.clear()
    }
    // プレイヤー一人一つ???
    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        val player = event.entity
        val tp = tps[player.uniqueId] ?: return
        tp.remove()
        tps.remove(player.uniqueId)
    }
    @EventHandler
    fun onLeave(event: PlayerQuitEvent) {
        val player = event.player
        val tp = tps[player.uniqueId] ?: return
        tp.remove()
        tps.remove(player.uniqueId)
    }
    @EventHandler
    fun onPlaceBlock(event: BlockPlaceEvent) {
        val loc = event.block.location
        if (!checkAbove(loc)) return
        event.isCancelled = true
    }
    private fun checkAbove(loc: Location): Boolean {
        tps.values().forEach {
            val clonedTp1y1 = it.loc1.location.clone().apply {y+=1}
            val clonedTp1y2 = it.loc1.location.clone().apply {y+=2}
            val clonedTp2y1 = it.loc2?.location?.clone()?.apply{y+=1}
            val clonedTp2y2 = it.loc2?.location?.clone()?.apply{y+=2}
            if (loc == clonedTp1y1) return true
            if (loc == clonedTp1y2) return true
            if (loc == clonedTp2y1) return true
            if (loc == clonedTp2y2) return true
        }
        return false
    }
    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_BLOCK) return
        val player = event.player
        if (!player.isSneaking) return
        val item = player.itemInHand ?: return
        if (!Game.isStart) return
        if (!SpecialItem.isTeleportItem(item)) return
        val team = player.team() ?: return
        if (!isPlaceableHeight(event.clickedBlock.location)) {
            player.sendMessage("${ChatColor.RED}最大${SpecialItem.maxHight}までしか設置できません!")
            return
        }
        if (!isPlaceableDistance(event.clickedBlock.location, team.objects.nexus.location)) {
            player.sendMessage("${ChatColor.RED}自軍から離れすぎています!")
            return
        }
        if (!isPlaceableBlock(event.clickedBlock.location)) {
            val tp = if (tps.contains(player.uniqueId)) {tps[player.uniqueId]} else {return}
            tp!!.remove()
            tps.remove(player.uniqueId)
            return
        }
        val block = event.clickedBlock ?: return
        // まだ設置していない
        if (!tps.contains(player.uniqueId)) {
            tps.add(TransPort(player.uniqueId, block))
        }
        // 片方設置済み
        else {
            val tp = tps[player.uniqueId]!!
            if (tp.loc2 != null) {
                tp.remove()
                tps.remove(player.uniqueId)
                return
            }
            // TP2つ目設置(開通)
            tp.loc2 = block
            if (handicapCapCoolDown.contains(player.uniqueId)) handicapCapCoolDown.remove(player.uniqueId)
            handicapCapCoolDown.add(CoolDown(player.uniqueId))
            val handItem = player.itemInHand.clone().apply { amount = 1}
            val inv = player.inventory
            inv.removeItem(handItem)
            player.sendMessage("${ChatColor.GRAY}利用可能まで${SpecialItem.handicapCapCoolDown}秒かかります")
        }
    }

    private fun isPlaceableHeight(location: Location): Boolean {
        return location.y < SpecialItem.maxHight
    }

    private fun isPlaceableDistance(from: Location, to: Location): Boolean {
        return Util.euclideanDistance(from, to) < SpecialItem.maxDisntance
    }

    private fun isPlaceableBlock(loc: Location): Boolean {
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
        if (ResourceBlocks.isResourceBlocks(loc.block.type)) return false
        if (getTransPortLocation(loc) != null) return false
        if (loc.block.type == Material.OBSIDIAN) return false
        if (loc.block.type == Material.ANVIL) return false
        if (loc.block.type == Material.WORKBENCH) return false
        if (loc.block.type == Material.ENCHANTMENT_TABLE) return false
        if (loc.block.type == Material.CHEST) return false
        val blockY1 = loc.clone().apply{y+=1}.block
        val blockY2 = loc.clone().apply{y+=2}.block
        if (!(blockY1.type == Material.AIR && blockY2.type == Material.AIR)) return false
        return true
    }
    private fun getTransPortLocation(loc: Location): TransPort? {
        tps.values().forEach {transPort ->  if (transPort.loc1.location == loc || transPort.loc2?.location == loc) return transPort}
        return null
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
        tps.values().forEach {
            if (it.loc2 == null) return@forEach
            val loc = if (it.loc1 == block) {it.loc2!! } else if (it.loc2 == block) {it.loc1} else return@forEach
            // tpがplayerが設置したものか検査する
            if (player.uniqueId == it.uuid) {
                // cooldown中だったら中止
                if (handicapCapCoolDown.contains(player.uniqueId)) {
                    player.sendMessage("${ChatColor.RED}利用可能まで${handicapCapCoolDown[player.uniqueId]!!.coolDown}秒")
                    return
                }
            }
            player.realTeleport(loc.location.apply { y += 1 }); player.world.playSound(player.location,
            Sound.ENDERMAN_TELEPORT,
            1f,
            1f)
        }
    }
    @EventHandler
    fun enemyPlayerInteract(event: PlayerInteractEvent) {
        if (event.action == Action.RIGHT_CLICK_AIR || event.action == Action.LEFT_CLICK_AIR) return
        val player = event.player
        val block  = event.clickedBlock
        if (block.type != Material.QUARTZ_ORE) return
        val tp = getTransPortLocation(block.location) ?: return
        if (player.uniqueId != tp.uuid) {
            tps.remove(tp)
        }
    }
    @EventHandler
    fun onBreak(event: BlockBreakEvent) {
        if (event.block.type != Material.QUARTZ_ORE) return
        val tp = getTransPortLocation(event.block.location) ?: return
        val player = event.player
        // 自分の
        if (tp.uuid != player.uniqueId) return
        tps.remove(tp)
        event.isCancelled = true
    }
}