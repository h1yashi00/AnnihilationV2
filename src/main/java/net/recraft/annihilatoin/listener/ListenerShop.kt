package net.recraft.annihilatoin.listener

import net.recraft.annihilatoin.objects.Game
import net.recraft.annihilatoin.objects.builder.Priceable
import net.recraft.annihilatoin.objects.menu.ShopBrewingMenu
import net.recraft.annihilatoin.objects.menu.ShopWeaponMenu
import net.recraft.annihilatoin.util.Util
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.ItemStack

class ListenerShop(private val shopBrewingMenu: ShopBrewingMenu, private val shopWeaponMenu: ShopWeaponMenu): Listener {
    @EventHandler
    fun onSignBreak(event: BlockBreakEvent) {
        val location = event.block.location
        if (Game.isBrewing(location) || Game.isWeaponShop(location))
            event.isCancelled = true
    }

    @EventHandler
    fun onSignClick(event: PlayerInteractEvent) {
        val type = event.clickedBlock?.type ?: return
        val location = if (type == Material.SIGN || type == Material.WALL_SIGN) { event.clickedBlock.location } else { return }
        if (Game.isWeaponShop(location)) {
            event.player.openInventory(shopWeaponMenu.createInventory())
        }
        if (Game.isBrewing(location)) {
            event.player.openInventory(shopBrewingMenu.createInventory())
        }
    }
    @EventHandler
    fun onBuyShopItem(event: InventoryClickEvent) {
        val inventory = event.inventory
        val shopItem = if (inventory.title == shopBrewingMenu.title || inventory.title == shopWeaponMenu.title) { event.currentItem ?: return } else { return }
        if (Util.isAir(shopItem)) return
        event.isCancelled = true
        val price = Priceable.getPrice(shopItem)
        val playerInventory = event.whoClicked.inventory
        if (playerInventory.contains(Material.GOLD_INGOT, price)) { playerInventory.removeItem(ItemStack(Material.GOLD_INGOT, price)) } else { return }
        playerInventory.addItem(shopItem)
    }
}