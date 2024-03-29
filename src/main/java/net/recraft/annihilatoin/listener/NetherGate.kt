package net.recraft.annihilatoin.listener

import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.objects.Game.setKitType
import net.recraft.annihilatoin.objects.Game.team
import net.recraft.annihilatoin.objects.kit.KitGenerator
import net.recraft.annihilatoin.objects.kit.KitType
import net.recraft.annihilatoin.objects.menu.KitMenu
import net.recraft.annihilatoin.realTeleport
import net.recraft.annihilatoin.util.Util
import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerPortalEvent
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.event.world.PortalCreateEvent
import org.bukkit.inventory.PlayerInventory
import org.bukkit.scheduler.BukkitRunnable

class NetherGate(val menu: KitMenu): Listener {
    companion object {
        val kitItemTitle = "Kit items"
    }
    @EventHandler
    fun portal(event: PortalCreateEvent) {
        Bukkit.getOnlinePlayers().forEach { if (it.itemInHand.type == Material.FLINT_AND_STEEL) {if (it.gameMode == GameMode.CREATIVE) {event.isCancelled = false; return} }}
        event.isCancelled = true
    }
    @EventHandler
    fun portal(event: PlayerPortalEvent) {
        if (event.cause != PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) return
        val player = event.player
        val team = player.team() ?: return
        player.realTeleport(team.objects.randomSpawn)
        openKitMenu(player)
    }

    fun openKitMenu(player: Player) {
        object: BukkitRunnable() {
            override fun run() {
                player.openInventory(menu.createInventory())
            }
        }.runTaskLater(Game.plugin, 10)
    }

    @EventHandler
    fun onInventoryInterecept(event: InventoryClickEvent) {
        if (event.clickedInventory?.title != menu.title) return
        if (Util.isAir(event.currentItem)) return
        event.isCancelled = true
        val player = event.whoClicked as Player
        KitType.values().forEach {
            val kit = KitGenerator.get(it)
            if (kit.icon == event.currentItem) {
                val inventory = Bukkit.createInventory(null, InventoryType.CHEST, kitItemTitle)
                player.setKitType(it)
                val team = player.team()
                kit.allItems(team!!).forEach { item ->
                    inventory.addItem(item)
                }
                kit.setInit(player)
                removeKitSpecificSoulBound(player.inventory)
                player.openInventory(inventory)
            }
        }
    }
    private fun removeKitSpecificSoulBound(inventory: PlayerInventory){
        inventory.forEach {
            if (it == null) return@forEach
            if (!SoulBound.isSoulbound(it)) return@forEach
            if (it.type == Material.WOOD_SWORD || it.type == Material.WOOD_AXE || it.type == Material.WOOD_PICKAXE) return@forEach
            inventory.remove(it)
        }
    }
}