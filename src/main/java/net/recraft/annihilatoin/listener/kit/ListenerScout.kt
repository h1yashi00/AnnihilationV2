package net.recraft.annihilatoin.listener.kit

import net.recraft.annihilatoin.listener.CoolDown
import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.objects.kit.KitType
import net.recraft.annihilatoin.objects.kit.Scout
import org.bukkit.*
import org.bukkit.block.Block
import org.bukkit.entity.*
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerFishEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.projectiles.ProjectileSource
import org.bukkit.util.Vector

class KitScout : Listener {
    private val combatTagCoolDown = CoolDown(
        coolDown = Scout.combatTagCoolDown,
        readyMsg = "Grappleが使用可能になりました｡",
        notReadyMsg = "Grapple使用可能まであと"
    ) {
    }
    @EventHandler
    fun onRightClickFishingRod(event: PlayerFishEvent) {
        val player = event.player
        if (Game.getPlayerData(player.uniqueId).kitType != KitType.SCOUT) return
        if (event.state == PlayerFishEvent.State.FISHING) return
        val handItem = event.player.itemInHand
        if (!Scout.isScoutFishingRod(handItem)) return
        val hookLocation = event.hook.location
        if (!isPlaceHookUp(hookLocation))  {
            player.sendMessage("${ChatColor.RED} Unable to grapple")
            return
        }
        if (!combatTagCoolDown.isReady(player)) {
            combatTagCoolDown.isNotReadyMsg(player)
            return
        }
        // activate ability!!

        val diff = hookLocation.toVector().subtract(player.location.toVector())
        val vel = Vector()
        val d: Double = hookLocation.distance(player.location)
        vel.x = (1.0 + 0.08 * d) * diff.x / d
        vel.y = (1.0 + 0.04 * d) * diff.y / d + 0.05 * d
        vel.z = (1.0 + 0.08 * d) * diff.z / d
        player.velocity = vel
        player.playSound(player.location, Sound.ZOMBIE_INFECT, 10.0F, 2.0F)
    }
    @EventHandler
    fun onDamageHook(event: EntityDamageByEntityEvent) {
        if (event.cause != EntityDamageEvent.DamageCause.PROJECTILE) return
        if (event.damager.type != EntityType.FISHING_HOOK) return
        event.isCancelled = true
    }
    @EventHandler(priority = EventPriority.HIGHEST)
    fun onDamagePlayer(event: EntityDamageByEntityEvent) {
        val player: Player= if (event.entity is Player) { event.entity as Player }else return
        if (Game.getPlayerData(player.uniqueId).kitType != KitType.SCOUT) return
        player.sendMessage("${ChatColor.GRAY}ダメージを受けました｡${Scout.combatTagCoolDown}秒Grappleが使用できません")
        combatTagCoolDown.add(player)
    }

    @EventHandler
    fun onPlayerSelectInventory(event: PlayerItemHeldEvent) {
        val player = event.player
        val pd = Game.getPlayerData(player.uniqueId)
        if (pd.kitType != KitType.SCOUT) return
        val currentItem = player.inventory.getItem(event.newSlot)
        val prevItem    = player.inventory.getItem(event.previousSlot)
        if (Scout.isScoutFishingRod(currentItem) || Scout.isScoutFishingRod(prevItem)) {
            player.world.playSound(player.location, Sound.NOTE_STICKS, 10.0F, 1.0F);
        }
        return
    }

    @EventHandler
    fun onDamage(event: EntityDamageEvent) {
        if (event.cause != EntityDamageEvent.DamageCause.FALL) return
        val player: Player = if (event.entity is Player)  {event.entity as Player} else {return}
        val pd = Game.getPlayerData(player.uniqueId)
        if (pd.kitType != KitType.SCOUT) return
        if (!Scout.isScoutFishingRod(player.itemInHand)) return
        player.sendMessage("${ChatColor.GRAY}fall damage was reduced")
        val damage = event.damage
        event.damage = damage/2
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
