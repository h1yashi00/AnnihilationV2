package net.recraft.annihilatoin.listener.menu

import net.recraft.annihilatoin.config.ConfigMap
import net.recraft.annihilatoin.objects.menu.AnniConfigMenu
import net.recraft.annihilatoin.objects.Game
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractEvent

class InventoryIntercept(private val configMap: ConfigMap): Listener {
    @EventHandler
    fun onPlaceBlock(event: PlayerInteractEvent) {
        if (event.action != Action.RIGHT_CLICK_BLOCK) return
        val slotItem = event.player.inventory.getItem(0) ?: return
        val specialWool = if (AnniConfigMenu.isSpecialWool(slotItem)) { slotItem } else { return }
        val team = when (specialWool) {
            AnniConfigMenu.redWool      -> Game.getTeam("red")  ?: return
            AnniConfigMenu.blueWool     ->Game.getTeam("blue")  ?: return
            AnniConfigMenu.yellowWool   ->Game.getTeam("yellow")?: return
            AnniConfigMenu.greenWool    ->Game.getTeam("green") ?: return
            else -> return
        }
        val holdingItem = event.item
        event.isCancelled = true
        val world = event.player.world
        val location = event.clickedBlock.location
        val objectName = when (holdingItem) {
            AnniConfigMenu.shop  ->  "shop"
            AnniConfigMenu.nexus -> "nexus"
            AnniConfigMenu.spawn1 ->  "spawn1"
            AnniConfigMenu.spawn2 ->  "spawn2"
            AnniConfigMenu.spawn3 ->  "spawn3"
            AnniConfigMenu.enderchest ->  "enderchest"
            AnniConfigMenu.enderfurnace ->  "enderfurnace"
            else -> return
        }
        val x = location.x.toInt()
        val y = location.y.toInt()
        val z = location.z.toInt()
        val locString = "$x,$y,$z"
        event.player.sendMessage(locString)
        configMap.write(world, objectName, team, locString)
    }
    @EventHandler
    fun onInventoryClickEvent(event: InventoryClickEvent) {
        if (!AnniConfigMenu.isSpecialWool(event.currentItem)) return
        event.isCancelled = true
    }
    @EventHandler
    fun onInventoryListener(event: InventoryClickEvent) {
        if (event.clickedInventory.title != AnniConfigMenu.title) return
        val player = if (event.whoClicked is Player) {Bukkit.getPlayer(event.whoClicked.name)} else { return }
         val colorWool = when(event.currentItem) {
            AnniConfigMenu.redWool -> AnniConfigMenu.redWool
            AnniConfigMenu.blueWool -> AnniConfigMenu.blueWool
            AnniConfigMenu.yellowWool -> AnniConfigMenu.greenWool
            AnniConfigMenu.greenWool -> AnniConfigMenu.greenWool
            else -> return
        }
        AnniConfigMenu.giveSpecificItems(colorWool, player)
        event.isCancelled = true
    }
}
