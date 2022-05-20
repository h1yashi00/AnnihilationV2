package net.recraft.annihilatoin.listener

import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.objects.kit.KitGenerator
import net.recraft.annihilatoin.objects.kit.KitType
import net.recraft.annihilatoin.objects.menu.AnniConfigMenu
import net.recraft.annihilatoin.objects.menu.KitMenu
import net.recraft.annihilatoin.util.Util
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.player.PlayerPortalEvent
import org.bukkit.event.player.PlayerTeleportEvent
import org.bukkit.event.world.PortalCreateEvent
import org.bukkit.scheduler.BukkitRunnable

class NetherGate(val menu: KitMenu): Listener {
    @EventHandler
    fun portal(event: PortalCreateEvent) {
        event.isCancelled = true
    }
    @EventHandler
    fun portal(event: PlayerPortalEvent) {
        if (event.cause != PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) return
        val player = event.player
        val team = Game.getPlayerData(player.uniqueId).team ?: return
        object: BukkitRunnable() {
            var counter = 0
            override fun run() {
                if (counter == 9) {
                    player.teleport(team.objects.randomSpawn)
                    openKitMenu(player)
                    cancel()
                }
                Bukkit.broadcastMessage("$counter")
                counter++
                if (!isPortal(player.location)) {
                    cancel()
                }
            }
        }.runTaskTimerAsynchronously(Game.plugin, 5, 10L)
    }
    fun isPortal(loc: Location): Boolean {
        val north = Location(loc.world, loc.x+1, loc.y, loc.z).block.type
        val east  = Location(loc.world, loc.x-1, loc.y, loc.z).block.type
        val west  = Location(loc.world, loc.x, loc.y, loc.z+1).block.type
        val south = Location(loc.world, loc.x, loc.y, loc.z-1).block.type

        if (north == Material.PORTAL || east == Material.PORTAL || west == Material.PORTAL || south == Material.PORTAL) return true
        return false
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
        val player = event.whoClicked
        KitType.values().forEach {
            val kit = KitGenerator.get(it)
            if (kit.icon == event.currentItem) {
                val inventory = Bukkit.createInventory(null, InventoryType.CHEST)
                val pd = Game.getPlayerData(player.uniqueId)
                pd.kitType = it
                kit.allItems(pd.team!!).forEach { item ->
                    inventory.addItem(item)
                }
                player.openInventory(inventory)
            }
        }
    }
}