package net.recraft.annihilatoin.listener.kit

import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.objects.kit.KitType
import net.recraft.annihilatoin.objects.kit.Scout
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Entity
import org.bukkit.entity.FishHook
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerFishEvent
import org.bukkit.projectiles.ProjectileSource
import org.bukkit.util.Vector

class KitScout : Listener {
    @EventHandler
    fun onRightClickFishingRod(event: PlayerFishEvent) {
        val player = event.player
        if (Game.getKit(player.uniqueId) != KitType.SCOUT) return
        if (event.state == PlayerFishEvent.State.FISHING) return
        val handItem = event.player.itemInHand
        if (!Scout.isScoutFishingRod(handItem)) return
        val hookLocation = event.hook.location
        if (!isPlaceHookUp(hookLocation)) return
        val diff = hookLocation.toVector().subtract(player.location.toVector())
        val vel = Vector()
        val d: Double = hookLocation.distance(player.location)
        vel.x = (1.0 + 0.08 * d) * diff.x / d
        vel.y = (1.0 + 0.04 * d) * diff.y / d + 0.05 * d
        vel.z = (1.0 + 0.07 * d) * diff.z / d
        player.velocity = vel
    }

    @EventHandler
    fun onScoutFishingRodDamage(event: EntityDamageByEntityEvent) {
        val hook: Entity = event.damager as? FishHook ?: return
        val player: ProjectileSource = (hook as FishHook).shooter as? Player ?: return
        val handItem = (player as Player).itemInHand
        val lore = handItem.itemMeta.lore ?: return
        if (!lore.contains(ChatColor.GOLD.toString() + "Scout")) return
        event.isCancelled = true
    }

    private fun isPlaceHookUp(location: Location): Boolean {
        val hook_X = location.blockX
        val hook_Y = location.blockY
        val hook_Z = location.blockZ
        val worldUid = location.world.uid
        val upper = Bukkit.getWorld(worldUid).getBlockAt(hook_X, hook_Y + 1, hook_Z)
        val below = Bukkit.getWorld(worldUid).getBlockAt(hook_X, hook_Y - 1, hook_Z)
        val north = Bukkit.getWorld(worldUid).getBlockAt(hook_X + 1, hook_Y, hook_Z)
        val south = Bukkit.getWorld(worldUid).getBlockAt(hook_X - 1, hook_Y, hook_Z)
        val east = Bukkit.getWorld(worldUid).getBlockAt(hook_X, hook_Y, hook_Z + 1)
        val west = Bukkit.getWorld(worldUid).getBlockAt(hook_X, hook_Y, hook_Z - 1)
        return !isAir(upper) || !isAir(below) || !isAir(north) || !isAir(south) || !isAir(east) || !isAir(west)
    }


    private fun isAir(block: Block): Boolean {
        return block.type === Material.AIR
    }

}
