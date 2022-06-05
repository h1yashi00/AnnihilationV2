package net.recraft.annihilatoin.listener.kit

import net.minecraft.server.v1_8_R3.EnumDirection
import net.recraft.annihilatoin.listener.CoolDown
import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.objects.Game.kitType
import net.recraft.annihilatoin.objects.Game.setVoidCancel
import net.recraft.annihilatoin.objects.Game.team
import net.recraft.annihilatoin.objects.kit.KitType
import net.recraft.annihilatoin.objects.kit.Swapper
import org.bukkit.*
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.entity.Firework
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scheduler.BukkitRunnable


class ListenerSwapper: Listener {
    val coolDown = CoolDown(
        Swapper.cooldown,
    ) {}

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_AIR && event.action != Action.RIGHT_CLICK_BLOCK) return
        if (event.item == null) return
        val player = event.player
        if (player.kitType() != KitType.SWAPPER) return
        if (!Swapper.isSwapItem(event.item)) return
        if (!coolDown.isReady(player)) {
            coolDown.isNotReadyMsg(player)
            return
        }
        // プレイヤーの正面にブロックがないか確認する
        val type = getPlayerFaceBlock(player)
        if (type != Material.AIR) return
        var loc = player.location.apply {y += 1}
        for (i in 0..16) {
            val direction = loc.direction
            // 0,1,2は 正面下のブロックも反応してしまうのでとばす
            if (!(i == 0 || i == 1 || i == 2)) { // 0,1,2じゃないとき検知
                if (loc.block.type != Material.AIR && i != 0) break
            }
            loc = loc.add(direction.x, direction.y, direction.z)
            val target = getPlayerByLocation(loc, 1.5F)?: continue
            if (target == player) continue
            if (player.team() == target.team()) continue
            // swap target from player!!!
            object: BukkitRunnable() {
                override fun run() {
                    // 5秒間Voidに行けない
                    target.setVoidCancel(false)
                }
            }.runTaskLater(Game.plugin, 20*5)
            target.setVoidCancel(true)
            coolDown.add(player)
            playSound(player, target)
            switchLocation(player, target)
            swappedEffect(target.location)
            target.addPotionEffect(PotionEffect(PotionEffectType.ABSORPTION, 20*5, 5/2))
        }
    }
    private fun getPlayerFaceBlock(player: Player): Material? {
        val headLoc = player.location.apply {y += 1}
        val direction = (player as CraftPlayer).handle.direction

        if (direction == EnumDirection.NORTH) {
            return headLoc.apply {z -= 1}.block.type
        }
        if (direction == EnumDirection.EAST) {
            return headLoc.apply {x += 1}.block.type
        }
        if (direction == EnumDirection.SOUTH) {
            return headLoc.apply {z += 1}.block.type
        }
        if (direction == EnumDirection.WEST) {
            return headLoc.apply {x -= 1}.block.type
        }
        return null
    }

    private fun swappedEffect(loc: Location) {
        val firework = loc.world.spawn(loc, Firework::class.java)
        val meta = firework.fireworkMeta
        meta.addEffect(FireworkEffect.builder().withColor(Color.YELLOW).withColor(Color.RED).withColor(Color.PURPLE).with(FireworkEffect.Type.BALL).build())
        meta.power = 0
        firework.fireworkMeta = meta
    }

    private fun playSound(p1: Player, p2: Player) {
        p1.playSound(p1.location, Sound.ENDERMAN_TELEPORT, 1F, 2F)
        p2.playSound(p1.location, Sound.ENDERMAN_TELEPORT, 1F, 2F)
    }
    private fun getPlayerByLocation(loc: Location, r: Float): Player? {
        loc.world.entities.forEach {
            if (it.location.distanceSquared(loc) < r)
                if (it is Player) return it.player
        }
        return null
    }
    private fun switchLocation(p1: Player, p2: Player) {
        val p1Loc = p1.location
        val p2Loc = p2.location
        p1.teleport(p2Loc)
        p2.teleport(p1Loc)
    }
}